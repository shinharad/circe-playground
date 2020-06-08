package example.part2.step1

import cats.implicits._

import io.circe._
// import io.circe.generic.auto._ // これは敢えて使わない
import io.circe.syntax._

import example._
import example.support.ReadJsonFrom

// Decoder/Encoderを自前で定義

// Decoder/Encoderを別々に定義
object CustomizingCodecWithDecoderEncoder extends App {

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
    import io.circe.Decoder

    //-------------------------------------------
    // implicit val decoder: Decoder[Data] = new Decoder[Data] {
    //   def apply(cursor: HCursor): Decoder.Result[Data] =
    //     (
    //       cursor.downField("key1").as[String],
    //       cursor.downField("key2").as[Int]
    //     ).mapN(Data.apply)
    // }

    // implicit val encoder: Encoder[Data] = new Encoder[Data] {
    //   def apply(data: Data): Json =
    //     Json.obj(
    //       "key1" -> Json.fromString(data.key1),
    //       "key2" -> Json.fromInt(data.key2)
    //     )
    // }

    //-------------------------------------------
    // SAMで簡潔に書き換える
    // implicit val decoder: Decoder[Data] = cursor =>
    //   (
    //     cursor.downField("key1").as[String],
    //     cursor.downField("key2").as[Int]
    //   ).mapN(Data.apply)

    // implicit val encoder: Encoder[Data] = data =>
    //   Json.obj(
    //     "key1" -> Json.fromString(data.key1),
    //     "key2" -> Json.fromInt(data.key2)
    //   )

    //-------------------------------------------
    // Decoderはcursor.getでより簡潔に書ける
    // implicit val decoder: Decoder[Data] = cursor =>
    //   (
    //     cursor.get[String]("key1"),
    //     cursor.get[Int]("key2")
    //   ).mapN(Data.apply)

    // implicit val encoder: Encoder[Data] = data =>
    //   Json.obj(
    //     "key1" -> Json.fromString(data.key1),
    //     "key2" -> Json.fromInt(data.key2)
    //   )

    //-------------------------------------------
    // decodeXXX/encodeXXXでcursorを省略する
    // implicit val decoder: Decoder[Data] =
    //   (
    //     Decoder.decodeString.at("key1"),
    //     Decoder.decodeInt.at("key2")
    //   ).mapN(Data.apply)

    // implicit val encoder: Encoder[Data] = data =>
    //   Json.obj(
    //     "key1" -> Encoder.encodeString(data.key1),
    //     "key2" -> Encoder.encodeInt(data.key2)
    //   )

    //-------------------------------------------
    // より簡潔な記述に
    implicit val decoder: Decoder[Data] =
      (
        Decoder[String].at("key1"),
        Decoder[Int].at("key2")
      ).mapN(Data.apply)

    implicit val encoder: Encoder[Data] = data =>
      Json.obj(
        "key1" -> Encoder[String].apply(data.key1),
        "key2" -> Encoder[Int].apply(data.key2)
      )

  }

  val fromResourceDecodedPerson: Either[Throwable, Data] =
    "data.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_ => println("-" * 100)))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}

// Codecで一度に定義
object CustomizingCodecWithCodec extends App {

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
    import io.circe.Decoder

    implicit val codec: Codec[Data] = new Codec[Data] {

      def apply(cursor: HCursor): Decoder.Result[Data] =
        (
          cursor.downField("key1").as[String],
          cursor.downField("key2").as[Int]
        ).mapN(Data.apply)

      def apply(data: Data): Json =
        Json.obj(
          "key1" -> Json.fromString(data.key1),
          "key2" -> Json.fromInt(data.key2)
        )
    }

  }

  val fromResourceDecodedPerson: Either[Throwable, Data] =
    "data.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_ => println("-" * 100)))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}
