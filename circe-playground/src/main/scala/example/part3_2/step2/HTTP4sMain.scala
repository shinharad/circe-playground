package example.part3_2.step2

import cats.data._
import cats.implicits._

import cats.effect.{ ExitCode, IO, IOApp }

import fs2.Stream

import org.http4s.HttpApp
import org.http4s.implicits._

import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS

import example._

// ~reStart
object HTTP4sMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val httpApp: HttpApp[IO] =
      NonEmptyChain(
        CORS(GreetingServerRoute[IO].Hello.post)
      ).reduceLeft(_ <+> _).orNotFound

    val server: IO[Nothing] =
      BlazeServerBuilder[IO]
        .bindHttp(8080)
        .withHttpApp(httpApp)
        .resource
        .use(_ => IO.never)

    for {
      fiber <- server.start
      _ <- fiber.join
    } yield ExitCode.Success
  }

}
