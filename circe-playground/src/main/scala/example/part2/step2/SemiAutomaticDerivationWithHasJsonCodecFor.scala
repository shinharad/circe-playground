package example.part2.step2

import cats.implicits._

import io.circe._
import io.circe.syntax._

import example._
import example.support.ReadJsonFrom
import example.support.HasJsonCodecFor

object SemiAutomaticDerivationWithHasJsonCodecFor extends App {
  println("-" * 100)

  final case class Data(key1: Key1, key2: Int) {
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

  object Data extends HasJsonCodecFor[Data]

  final case class Key1(value: String) extends AnyVal
  object Key1 {
    implicit val decoder: Decoder[Key1] = Decoder[String].map(apply)
    implicit val encoder: Encoder[Key1] = Encoder[String].contramap(_.value)
  }

  val fromResourceDecodedPerson: Either[Throwable, Data] =
    "data.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_ => println("-" * 100)))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  roundTrip(Key1("whatever"))

  println("-" * 100)

}
