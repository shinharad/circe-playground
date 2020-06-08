package example.part1.step1

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

class GettingStarted {

  println("-" * 100)

  final case class Person(name: String, age: Int)

  val person: Person =
    Person("Alice", 27)

  val personEncodedAsJsonConvertedToString: String =
    Encoder[Person].apply(person).toString
  // Encoder[Person].apply(person).deepDropNullValues.toString
  // Encoder[Person].apply(person).spaces2SortKeys

  println(personEncodedAsJsonConvertedToString)

  val someString: String =
    """
    {
      "name" : "Alice",
      "age" : 27
    }
    """

  val fromStringDecodedPerson: Either[Error, Person] =
    decode[Person](someString)

  println(fromStringDecodedPerson)

  println("-" * 100)

}
