package lert.core.config

import java.io.InputStream
import java.nio.file.{Files, Path, Paths}
import javax.inject.{Inject, Named}

import com.fasterxml.jackson.databind.ObjectMapper
import lert.core.config.ConfigProvider._

trait ConfigParser {
  def read(is: InputStream): Config
}

class JsonConfigParser @Inject()(objectMapper: ObjectMapper) extends ConfigParser {
  def read(is: InputStream): Config = {
    objectMapper.readValue(is, classOf[Config])
  }
}

trait ConfigProvider {
  def config: Config

  def getLertHome(): Path = {
    val lertHome = Option(config.home).map(Paths.get(_)).getOrElse(Paths.get(HOME_DIR, LERT_TEMP_DIR))
    if (!Files.exists(lertHome)) {
      Files.createDirectories(lertHome)
    }
    lertHome
  }
}

object ConfigProvider {
  private val HOME_DIR = System.getProperty("user.home")
  private val LERT_TEMP_DIR = ".l3rt"
}

class SimpleConfigProvider(val config: Config) extends ConfigProvider

object SimpleConfigProvider {
  def apply(config: Config) = new SimpleConfigProvider(config)
}

class FileConfigProvider @Inject()(configReader: ConfigParser,
                                   argumentProvider: ArgumentProvider) extends ConfigProvider {
  override def config: Config = {
    val inputStream = Files.newInputStream(Paths.get(argumentProvider.arguments.config))
    try {
      configReader.read(inputStream)
    } finally {
      inputStream.close()
    }
  }
}