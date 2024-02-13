/*
* Copyright 2021 Kári Magnússon
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package kuzminki.akka.play.module

import javax.inject._
import akka.actor.ActorSystem
import play.api.Configuration
import play.api.inject.ApplicationLifecycle
import kuzminki.api._


object KuzminkiProvider {

  def parse(conf: Configuration) = {
    DbConfig
      .forDb(conf.get[String]("db"))
      .withUser(conf.get[String]("user"))
      .withPassword(conf.get[String]("password"))
      .withHost(conf.getOptional[String]("host").getOrElse("localhost"))
      .withPort(conf.getOptional[Int]("port").getOrElse(5432))
      .withPoolSize(conf.getOptional[Int]("poolsize").getOrElse(10))
  }
}

class KuzminkiProvider(conf: Configuration) extends Provider[Kuzminki] {
  @Inject private var lifecycle: ApplicationLifecycle  = _
  @Inject private var system: ActorSystem = _

  lazy val get: Kuzminki = {

    val dbConf = KuzminkiProvider.parse(conf)

    val ec = system.dispatchers.lookup(
      conf
        .getOptional[String]("dispatcher")
        .getOrElse("akka.actor.default-blocking-io-dispatcher")
    )

    val db = Kuzminki.create(dbConf, ec)
    lifecycle.addStopHook(() => db.close)
    db
  }
}

class KuzminkiSplitProvider(masterConf: Configuration,
                            slaveConf: Configuration,
                            ecOpt: Option[String]) extends Provider[Kuzminki] {
  
  @Inject private var lifecycle: ApplicationLifecycle  = _
  @Inject private var system: ActorSystem = _

  private def parse(conf: Configuration) = {
    if (conf.keys.contains("dispatcher")) {
      throw KuzminkiError(
        "dispatcher should be defined at the same level as 'master' and 'slave'"
      )
    }
    KuzminkiProvider.parse(conf)
  }

  lazy val get: Kuzminki = {

    val db = Kuzminki.createSplit(
      parse(masterConf),
      parse(slaveConf),
      system.dispatchers.lookup(ecOpt.getOrElse("akka.actor.default-blocking-io-dispatcher"))
    )

    lifecycle.addStopHook(() => db.close)
    db
  }
}























