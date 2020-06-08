package example.part1.step2

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import cats.implicits._

import example._
import example.support.ReadJsonFrom

// CamelCaseは普通にcodecできる
object CodecResourceWithCamelCase extends App {

  println("-" * 100)

  final case class Data(
      key1: String,
      key2: Int,
      key3: Boolean,
      key4CamelCase: Boolean
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

  val fromResourceDecodedPersonV3: Either[Throwable, Data] =
    "data-camelcase.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}
