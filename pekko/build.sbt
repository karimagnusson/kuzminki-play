scalaVersion := "2.13.12"

name := "kuzminki-pekko-play"

version := "0.9.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

lazy val root = (project in file("."))
  .settings(
    name := "kuzminki-zio",
    libraryDependencies ++= Seq(
      "io.github.karimagnusson" % "kuzminki-ec" % "0.9.4",
      "org.playframework" %% "play" % "3.0.1"
    )
  )

