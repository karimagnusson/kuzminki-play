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

package kuzminki.pekko.play.module

import play.api.inject.Module
import play.api.{Configuration, Environment}
import kuzminki.api.Kuzminki


class KuzminkiModule extends Module {

  def bindings(env: Environment, conf: Configuration) = {
    val dbConf = conf.get[Configuration]("kuzminki")
    val provider = new KuzminkiProvider(dbConf)
    Seq(bind[Kuzminki].to(provider))
  }
}

class KuzminkiMultiModule extends Module {

  def bindings(env: Environment, conf: Configuration) = {
    val multiConf = conf.get[Configuration]("kuzminki-multi")
    multiConf.subKeys.toSeq.map { name =>
      val dbConf = multiConf.get[Configuration](name)
      val provider = new KuzminkiProvider(dbConf)
      name match {
        case "default" =>
          bind[Kuzminki].to(provider)
        case _ =>
          bind[Kuzminki].qualifiedWith(name).to(provider)
      }
    }
  }
}

class KuzminkiSplitModule extends Module {
  
  def bindings(env: Environment, conf: Configuration) = {
    val splitConf = conf.get[Configuration]("kuzminki-split")
    val masterConf = splitConf.get[Configuration]("master")
    val slaveConf = splitConf.get[Configuration]("slave")
    val ecOpt = splitConf.getOptional[String]("dispatcher")
    val provider = new KuzminkiSplitProvider(masterConf, slaveConf, ecOpt)
    Seq(bind[Kuzminki].to(provider))
  }
}