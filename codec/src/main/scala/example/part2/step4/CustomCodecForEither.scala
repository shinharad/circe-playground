package example.part2.step4

import cats.implicits._

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

import example._
import example.support.ReadJsonFrom

object CustomCodecForEither extends App {

  println("-" * 100)

  //---------------------------------------------
  // autoでそのままencodeすると意図した挙動にならない

  val right1: Right[String, Int] = Right(1337)
  val right2: Either[String, Int] = Right(1337).withLeft[String]

  printlnGood(right1.asJson)
  // {
  // "value" : 1337
  // }

  println("-" * 100)

  printlnGood(right2.asJson)
  // {
  // "Right" : {
  //  "value" : 1337
  // }
  // }

  println("-" * 100)

  //---------------------------------------------
  // package.jsonにEitherのdecoder/encoderの定義を追加すると

  printlnGood(right1.asJson)
  // {
  // "value" : 1337
  // }

  println("-" * 100)

  printlnGood(right2.asJson)
  // 1337

  println("-" * 100)

  final case class Data(
      `key1-kebab-case`: String,
      key2: Int,
      key3: Either[String, Int] // StringかIntのUnion Typesを表現
    )

  object Data

  val fromResourceDecodedPersonV3: Either[Throwable, Data] =
    "data-codec-for-either.json"
      .pipe(ReadJsonFrom.resourceInto[Data])
      .tap(_.bimap(printlnBad, printlnGood))
      .tap(_.map(_.asJson.pipe(printlnGood)))

  println("-" * 100)

}
