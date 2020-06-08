package example.part2.step1

import cats.implicits._

import io.circe._
import io.circe.Decoder
import io.circe.syntax._

import example._
import example.support.ReadJsonFrom

object CodecsForProducts extends App {
  println("-" * 100)

  final case class Data(key1: String, key2: Int) {
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

  object Data {

    implicit val decoder: Decoder[Data] =
      Decoder.forProduct2(
        "key1",
        "key2"
      )(apply)

    implicit val encoder: Encoder[Data] =
      Encoder.forProduct2(
        "key1",
        "key2"
      )(unapply(_).get)

  }

  val fromResourceDecodedPerson: Either[Throwable, Data] =
    "data.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_ => println("-" * 100)))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}
