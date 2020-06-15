package example.goldenplayground

final class ExampleJsonSuite extends JsonTestSuite {
  // checkAll("Cocec[Person]", CodecTests[Person].codec)

  // Golden Testing
  checkAll("GoldenCocec[Person]", GoldenCodecTests[Person].goldenCodec)
  checkAll("GoldenCocec[Data]", GoldenCodecTests[Data].goldenCodec)
}
