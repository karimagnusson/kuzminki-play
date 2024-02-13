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

package kuzminki.akka.play.json

import play.api.libs.json._
import kuzminki.api.Jsonb


trait PlayJson {

  implicit val loadJson: Seq[Tuple2[String, Any]] => JsValue = { data =>
    PlayJsonLoader.load(data)
  }

  implicit val jsValueToJsonb: JsValue => Jsonb = obj => {
    Jsonb(Json.stringify(obj))
  }

  implicit val jsonbTojsValue: Jsonb => JsValue = obj => {
    Json.parse(obj.value)
  }
}




