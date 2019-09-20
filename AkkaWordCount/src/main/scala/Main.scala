import akka.actor.{ActorSystem, Props}
import messages.ShowMap

object Main extends App {
  val actorSystem = ActorSystem("WordCountSystem")
  val controller = actorSystem.actorOf(Props[WordCountCollector])
  controller ! """c:\work\novels"""
  Thread.sleep(3000)
  controller ! ShowMap
}
