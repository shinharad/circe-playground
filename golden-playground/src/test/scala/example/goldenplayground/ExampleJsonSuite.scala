package example.goldenplayground

final class ExampleJsonSuite extends JsonTestSuite {
  // checkAll("Cocec[Person]", CodecTests[Person].codec)
  checkAll("GoldenCocec[Person]", GoldenCodecTests[Person].goldenCodec)
}
