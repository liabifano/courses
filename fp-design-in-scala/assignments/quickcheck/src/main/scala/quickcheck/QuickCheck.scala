package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._
import Math._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min: insert min element") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("min: insert in empty") = forAll { a: Int =>
    findMin(insert(a, empty)) == a
  }

  property("insert and remove in empty") = forAll { a: Int =>
    isEmpty(deleteMin(insert(a, empty)))
  }

  property("min: meld two heaps") = forAll { (h1: H, h2: H) =>
    val minH1H2 = min(findMin(h1), findMin(h2))
    val minHsMeld = findMin(meld(h1, h2))
    minHsMeld == minH1H2
  }

  property("meld: two heaps") = forAll { (h1: H, h2: H) =>
    toList(meld(h1, h2)) == toList(meld(h2, h1))
  }

  property("associative meld: two heaps") = forAll { (h1: H, h2: H, h3: H) =>
    toList(meld(meld(h1, h2), h3)) == toList(meld(meld(h3, h2), h1))
  }

  property("insert: two elements") = forAll { (a: Int, b: Int) =>
    val minAB = min(a,b)
    val h = insert(b, insert(a, empty))
    findMin(h) == minAB
  }

  property("ording") = forAll { (h:H) =>
    toList(h) == toList(h).sorted
  }

  property("min: insert element great than min") = {
    val b = Int.MaxValue+1
    val h = insert(b+1, insert(b, insert(b+2, empty)))
    val hRemovingMin = deleteMin(h)
    hRemovingMin == insert(b+2, insert(b+1, empty))
  }


  lazy val genHeap: Gen[H] = for {
    k <- arbitrary[A]
    h <- frequency((1, empty), (1, genHeap))
  } yield insert(k, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  def toList(h: H): List[A] = h match {
    case empty => Nil
    case h => findMin(h) :: toList(deleteMin(h))
  }
}
