[![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/bukotsunikki.svg?style=social&label=Follow%20%40kuzminki_lib)](https://twitter.com/kuzminki_lib)

# kuzminki-play

#### About
This project contains a module to use [kuzminki-ec](https://github.com/karimagnusson/kuzminki-ec) with [Play Framework](https://www.playframework.com/). It also contains integration with the Play Json library. Take a look at the [kuzminki-play-demo](https://github.com/karimagnusson/kuzminki-play-demo) for an example of usage.

If you are using Akka, you can use version [0.9.3](https://github.com/karimagnusson/kuzminki-play/releases/tag/0.9.3)

#### Sbt
```sbt
// available for Scala 2.13 and Scala 3
libraryDependencies += "io.github.karimagnusson" %% "kuzminki-play" % "0.9.5"
```

#### Configuration
```sbt
play.modules.enabled += "kuzminki.pekko.play.module.KuzminkiModule"

kuzminki = {
  db = "<DB_NAME>"
  user = "<USER>"
  password = "<PASS>"
  host = "localhost"
  port = 5432
  maxPoolSize = 10
  minPoolSize = 3
}
```

#### Multiple databases
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
           db: Kuzminki) extends BaseController
                            with PlayJson { // implicit conversions for Play Json
```

#### Master / slave
Master / slave configuration provides one api where all SELECT queries will go to the slave and all others to the master. If a dispatcher is defined it should be under kuzminki-split.
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





