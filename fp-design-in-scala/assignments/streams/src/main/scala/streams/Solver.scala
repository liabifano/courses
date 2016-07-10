package streams

import common._

trait Solver extends GameDef {

  def done(b: Block): Boolean = {
    if (b.isStanding) { // b1 == b2
      if (b.b1 == goal) true
      else false
    }
    else false
  }

  def neighborsWithHistory(b: Block, history: List[Move]): Stream[(Block, List[Move])] = b.legalNeighbors.map(p => (p._1, p._2 :: history)).toStream

  def newNeighborsOnly(neighbors: Stream[(Block, List[Move])],
                       explored: Set[Block]): Stream[(Block, List[Move])] = neighbors.filter(x => !explored.contains(x._1))

  def from(initial: Stream[(Block, List[Move])],
           explored: Set[Block]): Stream[(Block, List[Move])] =
    if (initial.isEmpty) Stream()
    else {
      val others = initial.flatMap(x => newNeighborsOnly(neighborsWithHistory(x._1, x._2), explored))
      val newExplored = explored ++ initial.map(_._1).toSet
      initial ++ from(others, newExplored)
    }

  lazy val pathsFromStart: Stream[(Block, List[Move])] =
    from(Stream((startBlock, List())), Set())

  lazy val pathsToGoal: Stream[(Block, List[Move])] = pathsFromStart.filter(x => x._1 == Block(goal, goal))

  lazy val solution: List[Move] =
    if (pathsToGoal.isEmpty) List()
    else pathsToGoal.head._2.reverse
}
