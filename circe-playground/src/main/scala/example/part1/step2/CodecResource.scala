package example.part1.step2

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import cats.implicits._

import example._
import example.support.ReadJsonFrom

object CodecResource extends App {

  println("-" * 100)

  final case class Data(
      key1: String,
      key2: Int,
      key3: Boolean,
      key4: Boolean,
      key5: Option[Int],
      key6: Set[Int],
      key7: Map[String, String],
      key8: SubData,
      key9: String,
      floating1: Double,
      floating2: BigDecimal,
      long1: Long,
      long2: BigInt
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

  final case class SubData(key1: String, key2: Int)

  // tapはpackage objectの自前定義を使用
  val fromResourceDecodedPerson: Either[Throwable, Data] =
    ReadJsonFrom
      .resourceInto[Data](resourceName = "data.json")
      .tap(println)

  // pipeで書き換える
  val fromResourceDecodedPersonV2: Either[Throwable, Data] =
    "data.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(println)

  // tapをprintlnをEither#bimapでそれぞれprintlnBad/printlnGoodを出し分ける
  val fromResourceDecodedPersonV3: Either[Throwable, Data] =
    "data.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}
