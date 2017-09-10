package lert.core

import java.util.concurrent.{Executors, TimeUnit}

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import lert.core.config.{ArgumentProvider, ConfigProvider}
import lert.core.rule.RuleLoader

class TaskManager(period: Long, task: Task) extends LazyLogging {
  private val scheduler = Executors.newSingleThreadScheduledExecutor()

  def start(): Unit = {
    scheduler.scheduleWithFixedDelay(task, 0, period, TimeUnit.MILLISECONDS)
    logger.info("Task manager has been started")
  }

  def stop(): Unit = {
    scheduler.shutdownNow()
    logger.info("Task manager has been stopped")
  }
}

class Task @Inject()(argumentProvider: ArgumentProvider,
                     ruleLoader: RuleLoader) extends Runnable with LazyLogging {
  override def run(): Unit = {
    try {
      val args = argumentProvider.arguments
      require(args.rules != null, "Rule location not specified")

      logger.debug("Task is being run")
      args
        .rules
        .split(";")
        .foreach(ruleLoader.process)
    } catch {
      case ex: Any =>
        logger.error(ex.getLocalizedMessage)
        logger.debug(ex.getLocalizedMessage, ex)
    }
  }

}
