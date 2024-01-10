| Twitter |
| --- |
| [![Twitter Follow](https://img.shields.io/twitter/follow/kuzminki_lib?label=follow&style=flat&logo=twitter&color=brightgreen)](https://twitter.com/kuzminki_lib) |

# kuzminki-play

#### About
This project contains a module to use [kuzminki-ec](https://github.com/karimagnusson/kuzminki-ec) with [Play Framework](https://www.playframework.com/). It also contains integration with the Play Json library. It is available for both [Pekko](https://pekko.apache.org/) and [Akka](https://akka.io/). Take a look at the [kuzminki-play-demo](https://github.com/karimagnusson/kuzminki-play-demo) for an example of usage.

#### Sbt
```sbt
// Pekko
libraryDependencies += "io.github.karimagnusson" % "kuzminki-pekko-play" % "0.9.1"

// Akka
libraryDependencies += "io.github.karimagnusson" % "kuzminki-akka-play" % "0.9.1"
```

#### Configuration
```sbt
play.modules.enabled += "kuzminki.pekko.play.module.KuzminkiModule"

kuzminki = {
  db = "<DB_NAME>"
  user = "<USER>"
  password = "<PASS>"
}
```