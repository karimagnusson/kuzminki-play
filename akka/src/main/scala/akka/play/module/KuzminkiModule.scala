package kuzminki.akka.play.module

import play.api.inject.Module
import play.api.{Configuration, Environment}
import kuzminki.api.Kuzminki


class KuzminkiModule extends Module {

  def bindings(env: Environment, conf: Configuration) = {
    val provider = new KuzminkiProvider(conf)
    Seq(bind[Kuzminki].to(provider))
  }
}