import java.io.File

import akka.actor.{Actor, ActorLogging}
import messages.{ParseFile, Report}

import scala.io.Source

class WordCounter extends Actor with ActorLogging {
  private[this] def wordCount(f: String):Map[String, Int] = {
    Source.fromFile(f).getLines().flatMap("""\b\w+\b""".r.findAllIn(_)).
      toSeq.groupMapReduce(identity)(_=>1)(_+_)
  }

  override def receive = {
    case ParseFile(name) => {
      log.info("Now working on {}", name)
      sender ! Report(wordCount(name))
    }
  }
}
