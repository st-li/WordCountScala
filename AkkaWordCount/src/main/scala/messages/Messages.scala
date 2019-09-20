package messages

case class ParseFile(name: String)
case class Report(set: Map[String, Int])
case object ShowMap