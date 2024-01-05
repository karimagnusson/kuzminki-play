scalaVersion := "2.13.12"

name := "kuzminki-akka-play"

version := "0.9.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

lazy val root = (project in file("."))
  .settings(
    name := "kuzminki-zio",
    libraryDependencies ++= Seq(
      "io.github.karimagnusson" % "kuzminki-ec" % "0.9.4",
      "com.typesafe.play" %% "play" % "2.9.1",
      "com.google.inject" % "guice" % "7.0.0"
    )
  )

