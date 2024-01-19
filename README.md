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

### In the latest push:  

#### multiple databases

```sbt
play.modules.enabled += "kuzminki.pekko.play.module.KuzminkiMultiModule"

kuzminki-multi = {
  default = {
    db = "some_db"
    user = "user"
    password = "pass"
  }
  other = {
    db = "other_db"
    user = "user"
    password = "pass"
  }
}
```

#### multiple databases
Support fo multiple databases. Database named 'default' will not be named.

```sbt
play.modules.enabled += "kuzminki.pekko.play.module.KuzminkiMultiModule"

kuzminki-multi = {
  default = {
    db = "some_db"
    user = "user"
    password = "pass"
  }
  other = {
    db = "other_db"
    user = "user"
    password = "pass"
  }
}
```

```scala
@Singleton
class SomeController @Inject()(
  val controllerComponents: ControllerComponents,
   @Named("other") otherDb: Kuzminki
)(implicit ec: ExecutionContext,
           db: Kuzminki) extends BaseController {
```

#### Master / slave
Master / slave configuration provides one api where all SELECT queries will go to the slave and all others to the master.
```sbt
play.modules.enabled += "kuzminki.pekko.play.module.KuzminkiSplitProvider"

kuzminki-split = {
  master = {
    db = "master_db"
    user = "user"
    password = "pass"
  }
  slave = {
    db = "slave_db"
    user = "user"
    password = "pass"
  }
}
```





