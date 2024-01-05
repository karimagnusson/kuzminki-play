package kuzminki.akka.play

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import akka.actor.ActorSystem
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import kuzminki.api._
import kuzminki.jdbc.JdbcExecutor
import kuzminki.render.{
  RenderedQuery,
  RenderedOperation
}


object KuzminkiPool {

  private var poolOpt: Option[JdbcExecutor] = None

  private def createPool(config: Configuration, system: ActorSystem): JdbcExecutor = {
    val conf = config.get[Configuration]("kuzminki")
    val kzConfig = DbConfig
      .forDb(conf.get[String]("db"))
      .withUser(conf.get[String]("user"))
      .withPassword(conf.get[String]("password"))
      .withHost(conf.getOptional[String]("host").getOrElse("localhost"))
      .withPort(conf.getOptional[Int]("port").getOrElse(5432))
      .withPoolSize(conf.getOptional[Int]("poolsize").getOrElse(10))
    
    val pool = new JdbcExecutor(
      new HikariDataSource(new HikariConfig(kzConfig.props)),
      system.dispatchers.lookup(
        conf
          .getOptional[String]("dispatcher")
          .getOrElse("pekko.actor.default-blocking-io-dispatcher")
      )
    )
    poolOpt = Some(pool)
    poolOpt.get
  }
  
  def getPool(config: Configuration, system: ActorSystem): JdbcExecutor = {
    poolOpt match {
      case Some(pool) => pool
      case None => createPool(config, system)
    }
  }

  def closePool(): Future[Unit] = {
    poolOpt match {
      case Some(pool) =>
        poolOpt = None
        pool.close()
      case None =>
    }
    Future.successful(())
  }
}


class KuzminkiPlay @Inject() (config: Configuration,
                              lifecycle: ApplicationLifecycle,
                              system: ActorSystem) extends Kuzminki {

  private val pool = KuzminkiPool.getPool(config, system)

  lifecycle.addStopHook { () =>
    KuzminkiPool.closePool()
  }

  def query[R](render: => RenderedQuery[R])(implicit ec: ExecutionContext): Future[List[R]] =
    pool.query(render)

  def queryAs[R, T](render: => RenderedQuery[R], transform: R => T)(implicit ec: ExecutionContext): Future[List[T]] = 
    pool.query(render).map(_.map(transform))

  def queryHead[R](render: => RenderedQuery[R])(implicit ec: ExecutionContext): Future[R] =
    pool.query(render).map(_.head)

  def queryHeadAs[R, T](render: => RenderedQuery[R], transform: R => T)(implicit ec: ExecutionContext): Future[T] =
    pool.query(render).map(_.head).map(transform)

  def queryHeadOpt[R](render: => RenderedQuery[R])(implicit ec: ExecutionContext): Future[Option[R]] =
    pool.query(render).map(_.headOption)

  def queryHeadOptAs[R, T](render: => RenderedQuery[R], transform: R => T)(implicit ec: ExecutionContext): Future[Option[T]] =
    pool.query(render).map(_.headOption.map(transform))

  def exec(render: => RenderedOperation)(implicit ec: ExecutionContext): Future[Unit] =
    pool.exec(render)

  def execNum(render: => RenderedOperation)(implicit ec: ExecutionContext): Future[Int] =
    pool.execNum(render)

  def execList(stms: Seq[RenderedOperation])(implicit ec: ExecutionContext): Future[Unit] =
    pool.execList(stms)

  def close: Future[Unit] = throw KuzminkiError("Connection pool closes automatically")
}











