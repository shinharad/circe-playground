package example.goldenplayground

import io.circe.testing.ArbitraryInstances

import org.typelevel.discipline.scalatest.FunSuiteDiscipline

trait JsonTestSuite
    extends TestSuite
       with FunSuiteDiscipline
       with ArbitraryInstances {
  final protected val CodecTests =
    io.circe.testing.CodecTests

  final protected val GoldenCodecTests =
    io.circe.testing.golden.GoldenCodecTests
}
