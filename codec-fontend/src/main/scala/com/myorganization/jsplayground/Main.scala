package com.myorganization
package jsplayground

import scala.util.chaining._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

object Main extends App {

  final case class Request(userName: String)
  final case class Response(greeting: String)

  import org.scalajs.dom.ext.Ajax

  import scala.concurrent.ExecutionContext.Implicits.global

  Ajax
    .post(
      url = "http://localhost:8080/hello",
      headers = Map("Content-Type" -> "application/json"),
      data = Request("Bob").asJson.noSpaces
    )
    .foreach(_.responseText.pipe(decode[Response]).tap(println))

}
