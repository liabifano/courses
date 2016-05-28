package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  import FunSets._

  test("contains...") {
    assert(contains(Set(1), 1))
    assert(contains(Set(1, 2), 1))
    assert(!contains(Set(1, 2), 3))
    assert(!contains(Set(), 1))
  }
  test("singletonSet...") {
    assert(singletonSet(1) == Set(1))
    assert(contains(singletonSet(2), 2))
    assert(!contains(singletonSet(3), 2))
  }
  test("union...") {
    val s = union(Set(1, 2), Set(1))
    val s_empty = union(Set(), Set())

    assert(contains(s, 2))
    assert(!contains(s, 3))
    assert(!contains(s_empty, 1))
  }
  test("intersect...") {
    val s = intersect(Set(1,2), Set(1))
    val s_empty = intersect(Set(1,2), Set())

    assert(contains(s, 1))
    assert(!contains(s_empty, 1))
  }

  test("diff...") {
    val s = diff(Set(1,2), Set(1))
    val s_full = diff(Set(1,2), Set())

    assert(contains(s, 2))
    assert(!contains(s, 1))
    assert(contains(s_full, 1))
    assert(contains(s_full, 2))
  }

  test("filter...") {
    val p_fn = (x: Int) => x > 0

    assert(contains(filter(Set(-1, 1), p_fn), 1))
    assert(!contains(filter(Set(-1, 1), p_fn), -1))
  }

  test("forall...") {
    val p_fn = (x: Int) => x > 0

    assert(forall(Set(1,2,3), p_fn))
    assert(!forall(Set(-1,2,3), p_fn))
  }

  test("exists...") {
   val p_fn = (x: Int) => x == 1

    assert(exists(Set(1,2,3), p_fn))
    assert(!exists(Set(2,3), p_fn))
  }

  test("map...") {
    val f_fn = (x: Int) => x + 1

    assert(contains(map(Set(1,2), f_fn), 3))
    assert(!contains(map(Set(1,2), f_fn), 1))
  }
}
