package example
package goldenplayground

import cats._
import cats.implicits._

import io.circe._
import io.circe.generic.semiauto._

final case class Person(name: String, age: Int)
// final case class Person(name: String, age: Int, email: String)
object Person {
  implicit val eq: Eq[Person] = Eq.fromUniversalEquals
  implicit val codec: Codec[Person] = deriveCodec
}

final case class Data(char: Char, boolean: Boolean)
object Data {
  implicit val eq: Eq[Data] = Eq.fromUniversalEquals
  implicit val codec: Codec[Data] = deriveCodec
}

object Main extends App {
  printlnHyphens(100)

  import io.circe.syntax._
  import io.circe.parser._

  Person("Alice", 27)
  // Person("Alice", 27, "a@b.com")
    .asJson
    .spaces2
    .tap(printlnGood)
    .pipe(decode[Person])
    .tapAs(printlnHyphens(100))
    .bimap(printlnBad, printlnGood)

  printlnHyphens(100)
}
