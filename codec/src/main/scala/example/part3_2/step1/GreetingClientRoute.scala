package example.part3_2.step1

import cats.effect.Sync

import fs2.Stream

import io.circe.syntax._

import org.http4s.circe._
import org.http4s.dsl.io.POST
import org.http4s.implicits._

import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl

import example._

object GreetingClientRoute {

  def apply[F[_]: Sync](client: Client[F]): GreetingClientRoute[F] =
    new GreetingClientRoute[F](client)

  class GreetingClientRoute[F[_]: Sync](client: Client[F])
      extends Http4sClientDsl[F] {
    object Hello {
      // pipeで書き換える
      def post(userName: String): F[GreetingServerRoute.Response] =
        POST(
          body = GreetingServerRoute.Request(userName).asJson,
          uri = uri"http://localhost:8080/hello"
        ).pipe(client.expect[GreetingServerRoute.Response])

      // def post(userName: String): F[GreetingServerRoute.Response] =
      //   client.expect[GreetingServerRoute.Response](
      //     req = POST(
      //       body = GreetingServerRoute.Request(userName).asJson,
      //       uri = uri"http://localhost:8080/hello"
      //     )
      //   )

    }
  }

}
