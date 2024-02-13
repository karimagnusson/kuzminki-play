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

import java.util.UUID
import java.sql.Time
import java.sql.Date
import java.sql.Timestamp
import play.api.libs.json._
import kuzminki.api.Jsonb


object PlayJsonLoader {

  val toJsValue: Any => JsValue = {
    case v: String      => JsString(v)
    case v: Boolean     => JsBoolean(v)
    case v: Short       => JsNumber(BigDecimal(v))
    case v: Int         => JsNumber(v)
    case v: Long        => JsNumber(v)
    case v: Float       => JsNumber(BigDecimal(v))
    case v: Double      => JsNumber(BigDecimal(v))
    case v: BigDecimal  => JsNumber(v)
    case v: Time        => Json.toJson(v)
    case v: Date        => Json.toJson(v)
    case v: Timestamp   => Json.toJson(v)
    case v: UUID        => JsString(v.toString)
    case v: Jsonb       => Json.parse(v.value)
    case v: Option[_]   => v.map(toJsValue).getOrElse(JsNull)
    case v: Seq[_]      => JsArray(v.map(toJsValue))
    case v: JsValue     => v
    case v: Any         => throw new Exception(s"Cannot convert to JsValue [$v]")
  }

  def load(data: Seq[Tuple2[String, Any]]): JsValue = {
    JsObject(data.map(p => (p._1, toJsValue(p._2))))
  }
}