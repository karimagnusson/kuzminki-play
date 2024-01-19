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

import java.util.Properties
import scala.concurrent.{Future, ExecutionContext}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import kuzminki.api._
import kuzminki.jdbc.JdbcExecutor
import kuzminki.render.{
  RenderedQuery,
  RenderedOperation
}


class SplitApi(masterConf: DbConfig, slaveConf: DbConfig, dbContext: ExecutionContext) extends Kuzminki {

  val setPool = new JdbcExecutor(
    new HikariDataSource(new HikariConfig(masterConf.props)),
    dbContext
  )

  val getPool = new JdbcExecutor(
    new HikariDataSource(new HikariConfig(slaveConf.props)),
    dbContext
  )

  def router(stm: String) = stm.split(" ").head match {
    case "SELECT" => getPool
    case _ => setPool
  }

  def query[R](render: => RenderedQuery[R])(implicit ec: ExecutionContext): Future[List[R]] = {
    val stm = render
    router(stm.statement).query(stm)
  }

  def queryAs[R, T](render: => RenderedQuery[R], transform: R => T)(implicit ec: ExecutionContext): Future[List[T]] = 
    query(render).map(_.map(transform))

  def queryHead[R](render: => RenderedQuery[R])(implicit ec: ExecutionContext): Future[R] =
    query(render).map(_.head)

  def queryHeadAs[R, T](render: => RenderedQuery[R], transform: R => T)(implicit ec: ExecutionContext): Future[T] =
    query(render).map(_.head).map(transform)

  def queryHeadOpt[R](render: => RenderedQuery[R])(implicit ec: ExecutionContext): Future[Option[R]] =
    query(render).map(_.headOption)

  def queryHeadOptAs[R, T](render: => RenderedQuery[R], transform: R => T)(implicit ec: ExecutionContext): Future[Option[T]] =
    query(render).map(_.headOption.map(transform))

  def exec(render: => RenderedOperation)(implicit ec: ExecutionContext): Future[Unit] =
    setPool.exec(render)

  def execNum(render: => RenderedOperation)(implicit ec: ExecutionContext): Future[Int] =
    setPool.execNum(render)

  def execList(stms: Seq[RenderedOperation])(implicit ec: ExecutionContext): Future[Unit] =
    setPool.execList(stms)

  def close: Future[Unit] = {
    setPool.close()
    getPool.close()
    Future.successful(())
  }
}












