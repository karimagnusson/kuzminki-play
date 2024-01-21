scalaVersion := "2.13.12"

name := "kuzminki-akka-play"

version := "0.9.2"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

lazy val root = (project in file("."))
  .settings(
    name := "kuzminki-akka-play",
    libraryDependencies ++= Seq(
      "io.github.karimagnusson" % "kuzminki-ec" % "0.9.4",
      "com.typesafe.play" %% "play" % "2.9.1"
    )
  )

