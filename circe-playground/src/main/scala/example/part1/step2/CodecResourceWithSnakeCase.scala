package example.part1.step2

import example._
import example.support.ReadJsonFrom

object CodecResourceWithSnakeCase1 extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.syntax._

  import cats.implicits._

  println("-" * 100)

  final case class Data(
      key1: String,
      key2: Int,
      key3: Boolean,
      key4_snake_case: Boolean
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
    "data-snakecase.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  // roundTrip(Key1("whatever"))

  println("-" * 100)

}

// circe-generic-extrasを使用して、snake caseのjsonをcamel caseのcase classに設定する
object CodecResourceWithSnakeCase2 extends App {
  import io.circe._
  // import io.circe.generic.auto._
  import io.circe.generic.extras._
  import io.circe.syntax._

  import cats.implicits._

  println("-" * 100)

  implicit val config: Configuration =
    Configuration
      .default
      .withSnakeCaseMemberNames

  @ConfiguredJsonCodec
  final case class Data(
      key1: String,
      key2: Int,
      key3: Boolean,
      key4SnakeCase: Boolean
      // key4_snake_case: Boolean
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
    "data-snakecase.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}
