package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 | c == r) {
      return 1
    }
    else {
      pascal(c, r - 1) + pascal(c - 1, r - 1)
    }
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def balance_checker(chars: List[Char], acc: Int): Boolean = {
      if (chars.isEmpty) {
        if (acc == 0) {
          return true
        }
        else {
          return false
        }
      }
      else {
        if (acc < 0) {
          return false
        }
        else if (chars.head == '(') {
          return balance_checker(chars.tail, acc + 1)
        }
        else if (chars.head == ')') {
          return balance_checker(chars.tail, acc - 1)
        }
        else {
          return balance_checker(chars.tail, acc)
        }
      }
    }
    return balance_checker(chars, 0)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money < 0 | coins.isEmpty) {
      return 0
    }
    else if (money == 0) {
      return 1
    }
    else {
      return countChange(money, coins.tail) + countChange(money - coins.head, coins)
    }
  }
}
