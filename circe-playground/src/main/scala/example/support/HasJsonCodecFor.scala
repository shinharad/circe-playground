package example.support

import io.circe._
import io.circe.generic.decoding.DerivedDecoder
import io.circe.generic.encoding.DerivedAsObjectEncoder
import io.circe.generic.semiauto._

import shapeless.Lazy

trait HasJsonCodecFor[A] {
  implicit def decoder(implicit d: Lazy[DerivedDecoder[A]]): Decoder[A] =
    deriveDecoder

  implicit def encoder(
      implicit
      e: Lazy[DerivedAsObjectEncoder[A]]
    ): Encoder[A] = deriveEncoder

  // final implicit def codec[A](
  //     implicit
  //     c: Lazy[DerivedAsObjectCodec[A]]
  //   ): Codec.AsObject[A] = c.value
}
