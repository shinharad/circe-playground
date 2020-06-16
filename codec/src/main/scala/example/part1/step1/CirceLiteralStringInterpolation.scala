package example.part1.step1

object UsingLiteralPart1 extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.literal._
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
    someString.pipe(decode[Person])

  println(fromStringDecodedPerson)

  // val alreadyJson: Json =
  //   json"""
  //   {
  //     "name" : "Alice",
  //     "age" : 27
  //   }
  //   """

  // val alreadyJson: Json =
  //   json"""$person"""

  // val alreadyJson: Json =
  //   json"""$person""" tap println

  val nameKey: String = "name"
  val ageValue: Int = 27

  val alreadyJson: Json =
    json"""
    {
      $nameKey : "Alice",
      "age" : $ageValue
    }
    """

  val fromJsonDecodedPerson: Either[Error, Person] =
    alreadyJson.pipe(_.as[Person])

  println("-" * 100)

  println(fromJsonDecodedPerson)

  println("-" * 100)
}

object UsingLiteralPart2 extends App {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.literal._
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
    someString.pipe(decode[Person])

  println(fromStringDecodedPerson)

  val notJson: String =
    """

      "name" : "Alice",
      "age" : 27
    }
    """

  val alreadyJson: Json =
    json"""$notJson"""

  val fromJsonDecodedPerson: Either[Error, Person] =
    alreadyJson.pipe(_.as[Person])

  println("-" * 100)

  println(fromJsonDecodedPerson)

  println("-" * 100)
}
