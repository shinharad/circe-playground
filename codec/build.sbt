import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  // "-Wunused:_",
  // "-Xfatal-warnings",
  "-Ymacro-annotations"
)

lazy val `circe-playground` =
  project
    .in(file("."))
    .settings(
      name := "codec",
      addCompilerPlugin(org.typelevel.`kind-projector`),
      libraryDependencies ++= Seq(
        "co.fs2" %% "fs2-core" % "2.2.2",
        "co.fs2" %% "fs2-io" % "2.2.2",
        "com.typesafe" % "config" % "1.4.0",
        "eu.timepit" %% "refined" % "0.9.13",
        "io.circe" %% "circe-config" % "0.8.0",
        "org.typelevel" %% "cats-core" % "2.1.1",
        "org.typelevel" %% "cats-effect" % "2.1.2"
      ),
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-core",
        "io.circe" %% "circe-fs2",
        "io.circe" %% "circe-generic-extras",
        "io.circe" %% "circe-generic",
        "io.circe" %% "circe-literal",
        "io.circe" %% "circe-parser",
        "io.circe" %% "circe-refined"
      ).map(_ % "0.13.0"),
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-blaze-client",
        "org.http4s" %% "http4s-blaze-server",
        "org.http4s" %% "http4s-circe",
        "org.http4s" %% "http4s-dsl"
      ).map(_ % "0.21.1"),
      libraryDependencies ++= Seq(
        com.github.alexarchambault.`scalacheck-shapeless_1.14`,
      //   org.scalacheck.scalacheck,
      //   org.scalatest.scalatest,
      //   org.scalatestplus.`scalatestplus-scalacheck`,
      //   "eu.timepit" %% "refined-scalacheck" % "0.9.13",
      //   "io.circe" %% "circe-golden" % "0.2.1"
      ).map(_ % Test),
      Compile / console / scalacOptions --= Seq(
        "-Wunused:_",
        "-Xfatal-warnings"
      ),
      scalafmtOnCompile := true,
      Test / console / scalacOptions :=
        (Compile / console / scalacOptions).value
    )
