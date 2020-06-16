import cats.implicits._

import io.circe._
import io.circe.syntax._
import io.circe.generic.extras.Configuration

package object example {

  final implicit class PipeAndTap[A](private val a: A) extends AnyVal {
    @inline final def pipe[B](ab: A => B): B = ab(a)
    @inline final def tap[U](au: A => U): A = { au(a); a }
    @inline final def tapAs[U](u: => U): A = { u; a }
  }

  final implicit class SeqOps[A](private val self: Seq[A]) extends AnyVal {
    def withOneBasedIndexSwapped: Seq[(Int, A)] =
      self.zipWithIndex.map(_.map(_ + 1).swap)
  }

  def roundTrip[A: Encoder: Decoder](a: A): Decoder.Result[A] =
    a.asJson
      .tap(printlnGood)
      // .tap(_ => println("-" * 100))
      .tapAs(println("-" * 100)) // call by name
      .pipe(_.as[A])
      .tap(_.bimap(printlnBad, printlnGood))

  def printlnGood(in: Any): Unit =
    println(Console.GREEN + in + Console.RESET)

  def printlnBad(in: Any): Unit =
    println(Console.RED + in + Console.RESET)

  implicit def EitherDecoder[L: Decoder, R: Decoder]: Decoder[Either[L, R]] =
    Decoder[R].map(Right.apply) or Decoder[L].map(Left.apply)

  implicit def EitherEncoder[L: Encoder, R: Encoder]: Encoder[Either[L, R]] = {
    case Left(l)  => Encoder[L].apply(l)
    case Right(r) => Encoder[R].apply(r)
  }
}
