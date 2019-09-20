import java.io.File

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.RoundRobinPool
import messages.{ParseFile, Report, ShowMap}

import scala.language.postfixOps
import scalaz._
import Scalaz._

class WordCountCollector extends Actor with ActorLogging {

  private var word_map = Map[String, Int]() withDefaultValue 0

  private def listFilesRecur(f: File): Array[File] = {
    val current = f.listFiles
    current ++ current.filter(_.isDirectory).flatMap(listFilesRecur)
  }
  val wordCounters = context.actorOf(RoundRobinPool(100).props(Props[WordCounter]))

  override def receive: Receive = {
    case dirName: String => {
      log.info("Parse all text file in {}", dirName)
      val fileList = listFilesRecur(new File(dirName))
        .filter(_.getName.endsWith(".txt"))
      fileList.foreach(file => wordCounters ! ParseFile(file.getAbsolutePath))
    }
    case Report(map) => word_map = word_map |+| map
    case ShowMap => log.info("The word count Map is {}", word_map)
  }
}
