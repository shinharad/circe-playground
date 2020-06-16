package example.part2.step2

import example.support.HasJsonCodecFor
import example._

// sbt -Duser.timezone=UTC
object MyADTCodecs extends App {

  sealed abstract class MyADT extends Product with Serializable
  object MyADT extends HasJsonCodecFor[MyADT] {
    case object MySingleton1 extends MyADT
    case object MySingleton2 extends MyADT
    case class MyProduct1(a: String, b: Int) extends MyADT
    case class MyProduct2(c: java.time.LocalDate, d: java.time.LocalDateTime)
        extends MyADT
  }

  println("-" * 100)

  val instance1: MyADT = MyADT.MySingleton1
  val instance2: MyADT = MyADT.MySingleton2
  val instance3: MyADT = MyADT.MyProduct1("hello world", 1337)
  val instance4: MyADT =
    MyADT.MyProduct2(java.time.LocalDate.now, java.time.LocalDateTime.now)

  println("-" * 100)
  roundTrip(instance1)
  println("-" * 100)
  roundTrip(instance2)
  println("-" * 100)
  roundTrip(instance3)
  println("-" * 100)
  roundTrip(instance4)
  println("-" * 100)

}
