package kuzminki.akka.play

import javax.inject.Inject
import com.google.inject.AbstractModule


class KuzminkiPlayModule extends AbstractModule {
  override def configure() = {
    bind(classOf[KuzminkiPlay]).asEagerSingleton()
  }
}
