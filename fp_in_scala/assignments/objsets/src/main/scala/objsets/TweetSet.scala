package objsets

import objsets.{TweetData, TweetReader}

class Tweet(val user: String, val text: String, val retweets: Int) {
  override def toString: String =
    "User: " + user + "\n" +
      "Text: " + text + " [" + retweets + "]"
}

trait TweetList {
  def head: Tweet

  def tail: TweetList

  def isEmpty: Boolean

  def foreach(f: Tweet => Unit): Unit =
    if (!isEmpty) {
      f(head)
      tail.foreach(f)
    }
}

object Nil extends TweetList {
  def head = throw new java.util.NoSuchElementException("head of EmptyList")

  def tail = throw new java.util.NoSuchElementException("tail of EmptyList")

  def isEmpty = true
}

class Cons(val head: Tweet, val tail: TweetList) extends TweetList {
  def isEmpty = false
}

abstract class TweetSet {

  def filter(p: Tweet => Boolean): TweetSet = this.filterAcc(p, new Empty)

  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet

  def union(that: TweetSet): TweetSet

  def mostRetweeted: Tweet

  def descendingByRetweet: TweetList = {
    if (isEmpty) Nil
    else {
      val most = mostRetweeted
      new Cons(most, remove(most).descendingByRetweet)
    }
  }

  def incl(tweet: Tweet): TweetSet

  def remove(tweet: Tweet): TweetSet

  def contains(tweet: Tweet): Boolean

  def foreach(f: Tweet => Unit): Unit

  def isEmpty: Boolean
}

class Empty extends TweetSet {
  def isEmpty = true

  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = acc

  def contains(tweet: Tweet): Boolean = false

  def incl(tweet: Tweet): TweetSet = new NonEmpty(tweet, new Empty, new Empty)

  def remove(tweet: Tweet): TweetSet = this

  def foreach(f: Tweet => Unit): Unit = ()

  def union(that: TweetSet) = that

  def mostRetweeted: Tweet = throw new NoSuchElementException()
}

class NonEmpty(elem: Tweet, left: TweetSet, right: TweetSet) extends TweetSet {
  def isEmpty = false

  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = {
    left.filterAcc(p, right.filterAcc(p, if (p(elem)) acc.incl(elem) else acc))
  }

  def contains(x: Tweet): Boolean =
    if (x.text < elem.text) left.contains(x)
    else if (elem.text < x.text) right.contains(x)
    else true

  def incl(x: Tweet): TweetSet = {
    if (x.text < elem.text) new NonEmpty(elem, left.incl(x), right)
    else if (elem.text < x.text) new NonEmpty(elem, left, right.incl(x))
    else this
  }

  def union(that: TweetSet): TweetSet = {
    that.filterAcc(x => true, this)
  }

  def remove(tw: Tweet): TweetSet =
    if (tw.text < elem.text) new NonEmpty(elem, left.remove(tw), right)
    else if (elem.text < tw.text) new NonEmpty(elem, left, right.remove(tw))
    else left.union(right)

  def foreach(f: Tweet => Unit): Unit = {
    f(elem)
    left.foreach(f)
    right.foreach(f)
  }

  def moreRetweets(t1: Tweet, t2: Tweet): Tweet = {
    if(t1.retweets > t2.retweets) t1 else t2
  }

  def mostRetweeted: Tweet = {
    if (right.isEmpty && left.isEmpty) elem
    else if (right.isEmpty) moreRetweets(elem, left.mostRetweeted)
    else if (left.isEmpty) moreRetweets(elem, right.mostRetweeted)
    else moreRetweets(moreRetweets(left.mostRetweeted, right.mostRetweeted), elem)
  }

}


object GoogleVsApple {
  val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
  val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")

  lazy val googleTweets: TweetSet = TweetReader.allTweets.filter(x => google contains x)
  lazy val appleTweets: TweetSet = TweetReader.allTweets.filter(x => apple contains x)

  /**
   * A list of all tweets mentioning a keyword from either apple or google,
   * sorted by the number of retweets.
   */
  lazy val trending: TweetList = googleTweets.union(appleTweets).descendingByRetweet
}

object Main extends App {
  // Print the trending tweets
  GoogleVsApple.trending foreach println
}
