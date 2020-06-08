package example.part2.step3

import cats.implicits._

import io.circe._
import io.circe.syntax._
import io.circe.generic.extras._

import example._
import example.support.ReadJsonFrom

object GenericExtras extends App {
  println("-" * 100)

  implicit val config: Configuration =
    Configuration
      .default
      .withKebabCaseMemberNames

  @ConfiguredJsonCodec
  final case class Data(
      key1KebabCase: Key1,
      @JsonKey("key2 with different name") key2: Int
    ) {
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

  final case class Key1(value: String) extends AnyVal
  object Key1 {
    implicit val decoder: Decoder[Key1] = Decoder[String].map(apply)
    implicit val encoder: Encoder[Key1] = Encoder[String].contramap(_.value)
  }

  val fromResourceDecodedPerson: Either[Throwable, Data] =
    "data-kebabcase2.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_ => println("-" * 100)))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  roundTrip(Key1("whatever"))

  println("-" * 100)

}
