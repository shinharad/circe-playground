package example.part3_2.step1

import cats.effect.Sync
import cats.implicits._

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

object GreetingServerRoute {

  def apply[F[_]: Sync]: GreetingServerRoute[F] =
    new GreetingServerRoute[F]

  class GreetingServerRoute[F[_]: Sync] extends Http4sDsl[F] {
    object Hello {
      val post: HttpRoutes[F] = HttpRoutes.of[F] {
        case request @ POST -> Root / "hello" =>
          request.as[Request].flatMap { request =>
            Ok(Response(s"Hello ${request.userName}").asJson)
          }
      }
    }
  }

  final case class Request(userName: String)
  object Request {
    implicit val decoder: Decoder[Request] = deriveDecoder
    implicit val encoder: Encoder[Request] = deriveEncoder

    implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, Request] = jsonOf
    implicit def entityEncoder[F[_]]: EntityEncoder[F, Request] = jsonEncoderOf
  }

  final case class Response(greeting: String)
  object Response {
    implicit val decoder: Decoder[Response] = deriveDecoder
    implicit val encoder: Encoder[Response] = deriveEncoder

    implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, Response] = jsonOf
    implicit def entityEncoder[F[_]]: EntityEncoder[F, Response] = jsonEncoderOf
  }

}
