package example.part3_1.step3

import java.nio.file.Paths

import cats.implicits._

import cats.effect.{ ExitCode, IO, IOApp }

import eu.timepit.refined.types.all._

import fs2.Stream

import io.circe._
import io.circe.generic.auto._
import io.circe.refined._

import example._
import example.support.ReadJsonFrom

object Fs2Integration extends IOApp {

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

  final case class Key1(value: NonEmptyString)
  object Key1 {
    implicit val decoder: Decoder[Key1] = Decoder[NonEmptyString].map(apply)
    implicit val encoder: Encoder[Key1] =
      Encoder[NonEmptyString].contramap(_.value)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val stream: Stream[IO, Data] =
      Paths
        .get("./target/scala-2.13/classes/data-array.json")
        .pipe(ReadJsonFrom.pathIntoStreamOf[IO, Data])

    // これはNG
    // val stream: Stream[IO, Data] =
    //   Paths
    //     .get(getClass.getClassLoader.getResource("data-array.json").toURI.tap(println))
    //     .pipe(ReadJsonFrom.pathIntoStreamOf[IO, Data])

    for {
      _ <- IO(println("-" * 100))
      _ <- stream.map(println).take(2).compile.drain
      _ <- IO(println("-" * 100))
    } yield ExitCode.Success
  }

}

object CatsEffectExample extends IOApp {

  // override def run(args: List[String]): IO[ExitCode] =
  //   IO(println("hello world")) *> IO.pure(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO(println("-" * 100))
      _ <- IO(println("hello world"))
      _ <- IO(println("-" * 100))
    } yield ExitCode.Success

}
