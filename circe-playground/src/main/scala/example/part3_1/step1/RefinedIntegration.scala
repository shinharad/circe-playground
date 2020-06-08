package example.part3_1.step1

import cats.implicits._

import eu.timepit.refined.types.all._

import io.circe._
import io.circe.syntax._
import io.circe.refined._
import io.circe.generic.extras._

import example._
import example.support.ReadJsonFrom

object RefinedIntegration extends App {

  println("-" * 100)

  implicit val config: Configuration =
    Configuration
      .default
      .withSnakeCaseMemberNames

  @ConfiguredJsonCodec
  final case class Data(key1: Key1, key2: PosInt) {
    final override def toString: String =
      s"""|$productPrefix(
          |$content
          |)""".stripMargin

    private[this] def content: String =
      productElementNames
        .zip(productIterator)
        .map {
          case (name, value) => s"  $name = $value"
        }
        .mkString("\n")
  }

  object Data

  // NonEmptyStringはvalue classでは使えない
  final case class Key1(value: NonEmptyString)
  // final case class Key1(value: NonEmptyString) extends AnyVal
  object Key1 {
    implicit val decoder: Decoder[Key1] = Decoder[NonEmptyString].map(apply)
    implicit val encoder: Encoder[Key1] =
      Encoder[NonEmptyString].contramap(_.value)
  }

  val fromResourceDecodedPerson: Either[Throwable, Data] =
    "data-refined-error.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_ => println("-" * 100)))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}
