package funsets

object FunSets {

  type Set = Int => Boolean

  def contains(s: Set, elem: Int): Boolean = s(elem)

  def singletonSet(elem: Int): Set = return Set(elem)

  def union(s: Set, t: Set): Set = (x => contains(s, x) || contains(t, x))

  def intersect(s: Set, t: Set): Set = (x => contains(s, x) && contains(t, x))

  def diff(s: Set, t: Set): Set = (x => contains(s, x) && !contains(t, x))

  def filter(s: Set, p: Int => Boolean): Set = (x => p(x))

  val bound = 1000

  def forall(s: Set, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (a == bound) true
      else if (!p(a) && contains(s,a)) false
      else iter(a + 1)
    }
    iter(-bound)
  }

  def not(p: Int => Boolean) : Int => Boolean = !p(_)
  def exists(s: Set, p: Int => Boolean): Boolean = !forall(s, not(p))

  def map(s: Set, f: Int => Int): Set = {
    def iter(a: Int, subset: Set): Set = {
      val elem = singletonSet(f(a))
      if (a==bound && contains(s,a)) {return elem}
      else if (a==bound) {return subset}
      else if (contains(s,a)) {return union(iter(a+1, subset), elem)}
      else {return iter(a+1, subset)}
    }
    iter(-bound, Set())
  }


  def toString(s: Set): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }


  def printSet(s: Set) {
    println(toString(s))
  }
}
