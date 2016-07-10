package streams

import common._

trait StringParserTerrain extends GameDef {

  val level: String

  def terrainFunction(levelVector: Vector[Vector[Char]]): Pos => Boolean = pos =>
      try {
        val element = levelVector(pos.x)(pos.y)
        element != '-'
      } catch {
        case e: IndexOutOfBoundsException => false
      }

  def findChar(c: Char, levelVector: Vector[Vector[Char]]): Pos = {
    val x = levelVector.indexWhere(x => x.contains(c))
    val y = levelVector(x).indexOf(c)
    Pos(x,y)
  }

  lazy val vector: Vector[Vector[Char]] =
    Vector(level.split("\n").map(str => Vector(str: _*)): _*)

  lazy val terrain: Terrain = terrainFunction(vector)
  lazy val startPos: Pos = findChar('S', vector)
  lazy val goal: Pos = findChar('T', vector)

}
