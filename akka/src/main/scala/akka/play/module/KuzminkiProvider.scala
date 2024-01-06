package kuzminki.akka.play.module

import javax.inject._
import akka.actor.ActorSystem
import play.api.Configuration
import play.api.inject.ApplicationLifecycle
import kuzminki.api._


object KuzminkiProvider {

  def parse(conf: Configuration, system: ActorSystem) = {

    val dbConf = DbConfig
      .forDb(conf.get[String]("db"))
      .withUser(conf.get[String]("user"))
      .withPassword(conf.get[String]("password"))
      .withHost(conf.getOptional[String]("host").getOrElse("localhost"))
      .withPort(conf.getOptional[Int]("port").getOrElse(5432))
      .withPoolSize(conf.getOptional[Int]("poolsize").getOrElse(10))
    
    val ec = system.dispatchers.lookup(
      conf
        .getOptional[String]("dispatcher")
        .getOrElse("pekko.actor.default-blocking-io-dispatcher")
    )


    (dbConf, ec)
  }
}


class KuzminkiProvider(conf: Configuration) extends Provider[Kuzminki] {
  @Inject private var lifecycle: ApplicationLifecycle  = _
  @Inject private var system: ActorSystem = _

  lazy val get: Kuzminki = {
    val kzConf = conf.get[Configuration]("kuzminki")
    KuzminkiProvider.parse(kzConf, system) match {
      case (dbConf, ec) =>
        val db = Kuzminki.create(dbConf, ec)
        lifecycle.addStopHook { () =>
          db.close
        }
        db
    }
  }
}










