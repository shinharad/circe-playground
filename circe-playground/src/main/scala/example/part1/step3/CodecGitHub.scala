package example.part1.step3

import cats.implicits._

import io.circe.generic.extras._

import example.support.ReadJsonFrom
import example._

// GitHub APIのレスポンスをデコードする
// レスポンスのsnake caseをcamel caseのcase classに設定する
object CodecGitHub extends App {
  println("-" * 100)

  implicit val config: Configuration =
    Configuration
      .default
      .withSnakeCaseMemberNames

  @ConfiguredJsonCodec
  final case class GithubResponse(name: String, license: Option[License])

  @ConfiguredJsonCodec
  final case class License(
      key: String,
      name: String,
      spdxId: String,
      url: Option[String],
      nodeId: String
    )

  val fromUrlDecodedGitHubResponse: Either[Throwable, Seq[GithubResponse]] =
    "https://api.github.com/users/shinharad/repos"
      .pipe(ReadJsonFrom.urlInto[Seq[GithubResponse]])
      .tap(_.bimap(printlnBad, _.withOneBasedIndexSwapped.foreach(printlnGood)))

  println("-" * 100)
}
