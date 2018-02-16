package config

import com.google.inject.{AbstractModule, Provides}
import free.interpreters.ComposedInterpreters
import play.api.{Configuration, Environment}

import scala.concurrent.ExecutionContext

class GuiceConfig(environment: Environment, configuration: Configuration)
  extends AbstractModule {

  override def configure() = ()

  @Provides
  def composedInterpreters(implicit ec: ExecutionContext): ComposedInterpreters = new ComposedInterpreters()

}