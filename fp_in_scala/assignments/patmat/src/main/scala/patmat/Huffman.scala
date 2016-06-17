package patmat

import common._


object Huffman {

  abstract class CodeTree
  case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree
  case class Leaf(char: Char, weight: Int) extends CodeTree

  def weight(tree: CodeTree): Int = {
    tree match {
      case Fork(l, r, lst, w) => w
      case Leaf(c, w) => w
    }
  }

  def chars(tree: CodeTree): List[Char] = {
    tree match {
      case Fork(l, r, lst, w) => lst
      case Leaf(c, w) => List(c)
      }
    }

  def makeCodeTree(left: CodeTree, right: CodeTree) =
    Fork(left, right, chars(left) ::: chars(right), weight(left) + weight(right))

  def string2Chars(str: String): List[Char] = str.toList

  def times(chars: List[Char]): List[(Char, Int)] = chars.groupBy(identity).mapValues(_.size).toList

  def makeOrderedLeafList(freqs: List[(Char, Int)]): List[Leaf] = freqs.sortBy(_._1).map(e => Leaf(e._1, e._2))

  def singleton(trees: List[CodeTree]): Boolean = (trees.size <= 1) & (trees.nonEmpty)

  def combine(trees: List[CodeTree]): List[CodeTree] = makeCodeTree(trees(0), trees(1)) :: trees.drop(2)

  def until(sing: List[CodeTree] => Boolean, comb: List[CodeTree] => List[CodeTree])(trees: List[CodeTree]): CodeTree = {
    if (sing(trees)) trees.head
    else until(sing, comb)(comb(trees))
  }

  def createCodeTree(chars: List[Char]): CodeTree = {
    val input = makeOrderedLeafList(times(chars))
    until(singleton, combine)(input)
  }

  type Bit = Int
  def decode(tree: CodeTree, bits: List[Bit]): List[Char] = {
    def decodePartial(treePartial: CodeTree, bitPartial: List[Bit]) : List[Char] = {
      treePartial match {
           case Leaf(c, _) if (bitPartial.isEmpty) => List(c)
           case Leaf(c, _) => c :: decodePartial(tree, bitPartial)
           case Fork(l, r, _, _) => if (bitPartial.head == 0) decodePartial(l, bitPartial.tail) else decodePartial(r, bitPartial.tail)
        }
      }
    decodePartial(tree, bits)
  }

  val frenchCode: CodeTree = Fork(Fork(Fork(Leaf('s',121895),Fork(Leaf('d',56269),Fork(Fork(Fork(Leaf('x',5928),Leaf('j',8351),List('x','j'),14279),Leaf('f',16351),List('x','j','f'),30630),Fork(Fork(Fork(Fork(Leaf('z',2093),Fork(Leaf('k',745),Leaf('w',1747),List('k','w'),2492),List('z','k','w'),4585),Leaf('y',4725),List('z','k','w','y'),9310),Leaf('h',11298),List('z','k','w','y','h'),20608),Leaf('q',20889),List('z','k','w','y','h','q'),41497),List('x','j','f','z','k','w','y','h','q'),72127),List('d','x','j','f','z','k','w','y','h','q'),128396),List('s','d','x','j','f','z','k','w','y','h','q'),250291),Fork(Fork(Leaf('o',82762),Leaf('l',83668),List('o','l'),166430),Fork(Fork(Leaf('m',45521),Leaf('p',46335),List('m','p'),91856),Leaf('u',96785),List('m','p','u'),188641),List('o','l','m','p','u'),355071),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u'),605362),Fork(Fork(Fork(Leaf('r',100500),Fork(Leaf('c',50003),Fork(Leaf('v',24975),Fork(Leaf('g',13288),Leaf('b',13822),List('g','b'),27110),List('v','g','b'),52085),List('c','v','g','b'),102088),List('r','c','v','g','b'),202588),Fork(Leaf('n',108812),Leaf('t',111103),List('n','t'),219915),List('r','c','v','g','b','n','t'),422503),Fork(Leaf('e',225947),Fork(Leaf('i',115465),Leaf('a',117110),List('i','a'),232575),List('e','i','a'),458522),List('r','c','v','g','b','n','t','e','i','a'),881025),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u','r','c','v','g','b','n','t','e','i','a'),1486387)
  val secret: List[Bit] = List(0,0,1,1,1,0,1,0,1,1,1,0,0,1,1,0,1,0,0,1,1,0,1,0,1,1,0,0,1,1,1,1,1,0,1,0,1,1,0,0,0,0,1,0,1,1,1,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1)
  def decodedSecret: List[Char] = decode(frenchCode, secret)

  def encode(tree: CodeTree)(text: List[Char]): List[Bit] = {
    def encodePartial(treePartial: CodeTree, textPartial: List[Char], accBits: List[Bit]): List[Bit]= {
      if (textPartial.isEmpty) accBits
      else {
        treePartial match {
          case Leaf(_, _) => encodePartial(tree, textPartial.tail, accBits)
          case Fork(l, r, _, _) => if (chars(l) contains textPartial.head) encodePartial(l, textPartial, 0::accBits) else encodePartial(r, textPartial, 1::accBits)
        }
      }
    }
    encodePartial(tree, text, List()).reverse
  }

  type CodeTable = List[(Char, List[Bit])]
  def codeBits(table: CodeTable)(char: Char): List[Bit] = table.filter(_._1 == char).map(_._2.head)

  def convert(tree: CodeTree): CodeTable = {
    val allChars = tree match {
      case Leaf(c, _) => List(c)
      case Fork(_,_,cs,_) => cs
    }
    allChars.map(e => (e, encode(tree)(List(e))))
  }

  def mergeCodeTables(a: CodeTable, b: CodeTable): CodeTable = {
    if (a.isEmpty) b
    else if (b.isEmpty) a
    else mergeCodeTables(b.head::a, b.tail)
  }

  def quickEncode(tree: CodeTree)(text: List[Char]): List[Bit] = {
    val table = convert(tree)
    text.flatMap(codeBits(table))
  }
}
