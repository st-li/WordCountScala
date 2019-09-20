import scala.collection.immutable.HashMap
import java.io.File
import scala.io.Source
import scala.language.postfixOps
import scalaz._
import Scalaz._

object Main extends App {
  if (args.length == 0)
    println("I need to know the directory you want to start!")
  else {
    def listFilesRecur(f: File): Array[File] = {
      val current = f.listFiles
      current ++ current.filter(_.isDirectory).flatMap(listFilesRecur)
    }

    def wordCount(f: File): Map[String, Int] = {
      Source.fromFile(f.getAbsoluteFile).getLines().flatMap("""\b\w+\b""".r.findAllIn(_)).
        toSeq.groupMapReduce(identity)(_ => 1)(_ + _)
    }

    val start = System.nanoTime()
    val fileList = listFilesRecur(new File("""c:\work\novels""")).filter(_.getName.endsWith(".txt"))
    val map = fileList.foldLeft(new HashMap[String, Int]() withDefaultValue 0)(_ |+| wordCount(_))

    val end = System.nanoTime()
    println(s"Time taken ${(end - start) / 1.0e9} seconds")
  }
}
  //other way to retrieve the map
  //method 1:
  //    val map = text.getLines.flatMap(...).toSeq.groupBy(identity).map(i=>(i._1, i._2.length))
  //method 2:
  //    val itr1 = text.getLines.flatMap(...).map((_, 1))
  //    val map = itr1.foldLeft(Map[String, Int]().withDefaultValue(0))((map, item)=>map + (item._1 -> (map(item._1)+1)))
