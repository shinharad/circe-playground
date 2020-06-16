package example.support

import java.net.URL

import scala.io._

import cats.data._
import cats.implicits._

import com.typesafe.config._

import io.circe.{ fs2 => _, _ }
import io.circe.parser._

object ReadJsonFrom {
  def urlInto[A: Decoder](url: String): Either[Throwable, A] =
    getUrl(url).flatMap(urlInto[A])

  private[this] def getUrl(url: String): Either[Throwable, URL] =
    Either.catchNonFatal(new URL(url))

  def urlInto[A: Decoder](url: URL): Either[Throwable, A] =
    fromURL(url)
      .map(_.getLines.mkString)
      .flatMap(decode[A])

  private[this] def fromURL(url: URL): Either[Throwable, BufferedSource] =
    Either.catchNonFatal(Source.fromURL(url))

  def resourceInto[A: Decoder](resourceName: String): Either[Throwable, A] =
    getResource(resourceName).flatMap(urlInto[A])

  private[this] def getResource(resourceName: String): Either[Throwable, URL] =
    Either.catchNonFatal(getClass.getClassLoader.getResource(resourceName))

  def urlIntoAccumulating[A: Decoder](url: String): EitherNec[Throwable, A] =
    getUrl(url)
      .toEitherNec
      .flatMap(urlIntoAccumulating[A])

  def urlIntoAccumulating[A: Decoder](url: URL): EitherNec[Throwable, A] =
    fromURL(url)
      .map(_.getLines.mkString)
      .toEitherNec
      .flatMap(
        decodeAccumulating[A](_)
          .toEither
          .leftMap(NonEmptyChain.fromNonEmptyList)
      )

  def resourceIntoAccumulating[A: Decoder](
      resourceName: String
    ): EitherNec[Throwable, A] =
    getResource(resourceName).toEitherNec.flatMap(urlIntoAccumulating[A])

  def defaultHoConfigIntoAccumulating[A: Decoder]: EitherNec[Throwable, A] =
    Either
      .catchNonFatal(ConfigFactory.load)
      .toEitherNec
      .flatMap(hoConfigIntoAccumulating[A])

  def hoConfigIntoAccumulating[A: Decoder](
      config: Config
    ): EitherNec[Throwable, A] =
    io.circe
      .config
      .parser
      .decodeAccumulating[A](config)
      .toEither
      .leftMap(NonEmptyChain.fromNonEmptyList)

  import java.nio.file.Path

  import cats.effect.{ Blocker, ContextShift, IO, Sync }

  import fs2.{ text, Stream }
  import fs2.io.{ file }

  import io.circe.fs2.{ decoder, stringArrayParser }

  def pathIntoStreamOf[F[_]: ContextShift: Sync, A: Decoder](
      path: Path
    ): Stream[F, A] =
    Stream.resource(Blocker[F]).flatMap { blocker =>
      file
        .readAll(path, blocker, 4096)
        .through(text.utf8Decode)
        .through(stringArrayParser)
        .through(decoder[F, A])
    }

  def pathIntoStreamOfIO[A](
      path: Path
    )(implicit
      d: Decoder[A],
      cs: ContextShift[IO]
    ): Stream[IO, A] =
    pathIntoStreamOf[IO, A](path)

}
