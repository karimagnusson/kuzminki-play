
inThisBuild(List(
  organization := "io.github.karimagnusson",
  homepage := Some(url("https://github.com/karimagnusson/kuzminki-play")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "karimagnusson",
      "Kari Magnusson",
      "kotturinn@gmail.com",
      url("https://github.com/karimagnusson")
    )
  )
))

ThisBuild / version := "0.9.3"
ThisBuild / versionScheme := Some("early-semver")

scalaVersion := "3.3.1"

lazy val scala3 = "3.3.1"
lazy val scala213 = "2.13.12"
lazy val supportedScalaVersions = List(scala213, scala3)

lazy val root = (project in file("."))
  .aggregate(kuzminkiAkkaPlay)
  .settings(
    crossScalaVersions := Nil,
    publish / skip := true
  )

lazy val kuzminkiAkkaPlay = (project in file("kuzminki-akka-play"))
  .settings(
    name := "kuzminki-akka-play",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      "io.github.karimagnusson" %% "kuzminki-ec" % "0.9.5-RC2",
      "com.typesafe.play" %% "play" % "2.9.1"
    ),
    scalacOptions ++= Seq(
      "-encoding", "utf8",
      "-feature",
      "-language:higherKinds",
      "-language:existentials",
      "-language:implicitConversions",
      "-deprecation",
      "-unchecked"
    ),
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _))  => Seq("-rewrite")
        case _             => Seq("-Xlint")
      }
    },
  )