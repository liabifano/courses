package forcomp


object Anagrams {

  type Word = String

  type Sentence = List[Word]

  type Occurrences = List[(Char, Int)]

  val dictionary: List[Word] = loadDictionary

  def wordOccurrences(w: Word): Occurrences = w.toLowerCase.groupBy(identity).mapValues(_.size).toList.sorted

  def sentenceOccurrences(s: Sentence): Occurrences = wordOccurrences(s.reduce(_++_))

  lazy val dictionaryByOccurrences: Map[Occurrences, List[Word]] = dictionary.groupBy(wordOccurrences(_))

  def wordAnagrams(word: Word): List[Word] = dictionaryByOccurrences(wordOccurrences(word))

  def combinations(occurrences: Occurrences): List[Occurrences] =
    occurrences.foldRight(List[Occurrences](Nil)) {
      case ((char, n), combAcc) => {
        combAcc ::: (for {
                     comb <- combAcc
                     i <- 1 to n
                     } yield (char, i) :: comb)
        }
    }

  def subtract(x: Occurrences, y: Occurrences): Occurrences = x.filter(o => !y.contains(o))

  def sentenceAnagrams(sentence: Sentence): List[Sentence] = ???
}
