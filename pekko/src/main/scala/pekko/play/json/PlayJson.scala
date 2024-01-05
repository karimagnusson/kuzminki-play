package kuzminki.pekko.play.json

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




