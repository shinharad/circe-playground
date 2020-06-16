package example.part1.step1

// decodeではなくparseでやる場合
object UsingParseInsteadOfDecode extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._

  println("-" * 100)

  final case class Person(name: String, age: Int)

  val someString: String =
    """
    {
      "name" : "Alice",
      "age" : 27
    }
    """

  val fromStringDecodedPerson: Either[Error, Person] =
    parse(someString).flatMap(Decoder[Person].decodeJson)

  println(fromStringDecodedPerson)

  println("-" * 100)

}

// pipeを使ってparse
object UsingParseAndPipeInsteadOfDecode extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.syntax._
  import scala.util.chaining._

  println("-" * 100)

  final case class Person(name: String, age: Int)

  val someString: String =
    """
    {
      "name" : "Alice",
      "age" : 27
    }
    """

  val fromStringDecodedPerson: Either[Error, Person] =
    someString // String
      .pipe(parse) // Either[ParsingFailure, JSON ADT]
      // .flatMap(_.as[Person]) // Either[Error, our ADT]
      .flatMap(Decoder[Person].decodeJson) // Either[Error, our ADT

  println(fromStringDecodedPerson)

  println("-" * 100)
}

object UsingHCursorDecode extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import scala.util.chaining._

  println("-" * 100)

  final case class Person(name: String, age: Int)

  val someString: String =
    """
    {
      "name" : "Alice",
      "age" : 27
    }
    """

  val fromStringDecodedPerson: Either[Error, Person] =
    someString // String
      .pipe(parse) // Either[ParsingFailure, Json]
      .map(HCursor.fromJson) // Cursor
      .flatMap(Decoder[Person].apply) // Either[Error, our ADT]

  println(fromStringDecodedPerson)

  println("-" * 100)

}

object UsingSyntaxEncode extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.syntax._
  import example._

  println("-" * 100)

  final case class Person(name: String, age: Int)

  val person: Person =
    Person("Alice", 27)

  val personEncodedAsJsonConvertedToString: String =
    person // our ADT
    .asJson // JSON ADT
    .spaces2 // String

  println(personEncodedAsJsonConvertedToString)

  println("-" * 100)
}

object UsingDecodeAndPipe extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import example._

  println("-" * 100)

  final case class Person(name: String, age: Int)

  val someString: String =
    """
    {
      "name" : "Alice",
      "age" : 27
    }
    """

  // 単純にこれで良い
  val fromStringDecodedPerson: Either[Error, Person] =
    someString.pipe(decode[Person])

  println(fromStringDecodedPerson)

  println("-" * 100)

}
