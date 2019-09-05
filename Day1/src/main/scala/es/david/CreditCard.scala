package es.david

import scala.util.Random

sealed trait CreditCard {

  import CreditCard._

  def number: String

  final def isValid: Boolean = isInstanceOf[Valid]

  final def isNotValid: Boolean = !isValid

  final override def toString: String =
    if (isNotValid) {
      val invalid = Console.RED + "Invalid" + Console.RESET
      s"""$invalid credit card number "$number" """
    } else {
      val valid = Console.GREEN + "Valid" + Console.RESET
      val (payload, checkDigit) = split(number)

      s"""$valid credit card number "$number" """
    }
}

object CreditCard extends (String => CreditCard) {

  object Invalid {
    private[CreditCard] def apply(number: String): Invalid = new Invalid(number)
  }

  object Valid {
    private[CreditCard] def apply(number: String): Valid = new Valid(number)
  }

  final case class Invalid private(number: String) extends CreditCard

  final case class Valid private(number: String) extends CreditCard

  def apply(number: String): CreditCard =
    if (isValid(number))
      Valid(number)
    else
      Invalid(number)

  private def deoesMathCheckOut(number: String): Boolean = {
    val (payload, checkDigit) = split(number)
    val sum = luhn(payload) + checkDigit
    sum % 10 == 0
  }

  private def luhn(payload: String): Int =
    payload
      .reverse //String
      .map(_.toString.toInt) // IndexedSeq[Int]
      .zipWithIndex // IndexedSeq[(Int,Int)]
      .map {
        case (digit, index) =>
          if (index % 2 == 0)
            digit * 2
          else
            digit
      } // IndexedSeq[Int]
      .map { number =>
        if (number > 9)
          number - 9
        else
          number
      } // IndexedSeq[Int]
      .sum

  private def isValid(number: String): Boolean =
    number != null &&
      number.nonEmpty &&
      number.forall(Character.isDigit) &&
      (MinimumLength to MaximumLength).contains(number.length) &&
      deoesMathCheckOut(number)

  private def CheckDigitLength = 1

  private def MinimumLength = 13

  private def MaximumLength = 19

  private def split(number: String): (String, Int) = {
    val payload = number.dropRight(CheckDigitLength)
    val checkDigit = number.takeRight(CheckDigitLength).toInt

    payload -> checkDigit
    (payload, checkDigit)
  }

  def apply(): Valid = Valid(generatorNumber)

  private def generatorNumber: String = {
    val payload = {
      val min: Int = MinimumLength - CheckDigitLength //12
      val max: Int = MaximumLength - CheckDigitLength //18
      val length = min + Random.nextInt(max - min + 1)

      def randomDigit: Int =
        Random.nextInt(10) // 0 to 9
      (1 to length) // FiniteRange
        .map(_ => randomDigit) // IndexedSeq[Int]
        .mkString //String
    }
    val checkDigit: Int =
      (10 - (luhn(payload) % 10)) % 10

    val number = payload + checkDigit
    if (isValid(number))
      number
    else
      sys.error(s"Bug: generated an in invalid number $number")
  }
}