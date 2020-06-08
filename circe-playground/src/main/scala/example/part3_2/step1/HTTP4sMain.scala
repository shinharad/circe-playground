package example.part3_2.step1

import cats.data._
import cats.implicits._

import cats.effect.{ ExitCode, IO, IOApp }

import fs2.Stream

import org.http4s.HttpApp
import org.http4s.implicits._

import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.client.blaze.BlazeClientBuilder

import example._

// ~reStart
object HTTP4sMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val httpApp: HttpApp[IO] =
      NonEmptyChain(
        GreetingServerRoute[IO].Hello.post
      ).reduceLeft(_ <+> _).orNotFound

    val server: IO[Nothing] =
      BlazeServerBuilder[IO]
        .bindHttp(8080)
        .withHttpApp(httpApp)
        .resource
        .use(_ => IO.never)

    val clientStream: Stream[IO, GreetingServerRoute.Response] = {
      import scala.concurrent.ExecutionContext.global
      BlazeClientBuilder[IO](global).stream.flatMap { client =>
        def post(userName: String): IO[GreetingServerRoute.Response] =
          GreetingClientRoute[IO](client).Hello.post(userName)

        Stream.eval(post("Alice"))

        Stream.evalSeq(
          List("Alice", "Bravo", "Charlie", "Delta").traverse(post)
        )

        // pipeで書き換える
        List("Alice", "Bravo", "Charlie", "Delta")
          .traverse(post)
          .pipe(Stream.evalSeq)
      }
    }

    // val clientStream: Stream[IO, String] =
    //   Stream.eval(IO.pure("hello world"))

    for {
      fiber <- server.start
      _ <- IO(println("-" * 100))

      _ <- clientStream.map(println).compile.drain
      _ <- IO(println("-" * 100))

      // 2件だけ取得
      _ <- clientStream
        .take(2)
        .evalMap(response => IO(println(response)))
        .compile
        .drain
      _ <- IO(println("-" * 100))
      // _ <- fiber.join
      _ <- fiber.cancel
    } yield ExitCode.Success
  }

}
