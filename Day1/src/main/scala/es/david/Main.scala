package es.david

object Main {

  def main(args: Array[String]): Unit = {
    println("-" * 50)
    code(args)
    println("-" * 50)
  }

  def code(args: Array[String]): Unit = {
    // println("Hola Mundo")
    // println(CreditCard("iiii").isValid)
    // println(CreditCard().isValid)

    args
      .headOption // Option[String]
      .map(CreditCard) // Option[CreditCard]
      .map(println) // Option[Unit]
      .getOrElse(runDemo)
  }

  private def runDemo(): Unit = {
    // println("runing demo")
    val validCard: CreditCard.Valid =
      CreditCard()
    println(validCard)
    println(validCard.number)
    println(validCard.isValid)

    println()

    val inValidCard: CreditCard =
      CreditCard("11111111111")
    println(inValidCard)
    println(inValidCard.number)
    println(inValidCard.isValid)

    println()
    val fakeNumber = 1 to 10 map (_ => CreditCard())
    println(fakeNumber.forall(_.isValid))
    val moreFake = Seq(
      "4674157446587300",
      "4929711438991640",
      "4916927533445862868",
      "5193802031749471",
      "5551277156043982",
      "2221006808820621",
      "375037451421803",
      "346348350810327",
      "370910568380416",
      "6011327197212740",
      "6011393428545860",
      "6011766238111792850",
      "3541344157984719",
      "3545109011158982",
      "3544786370795995472",
      "5467172021970175",
      "5493619285920948",
      "5573051910988269",
      "30458540890716",
      "30074065876525",
      "30484322892357",
      "36215311971487",
      "36798060356828",
      "36647006616219",
      "5893633777617777",
      "6759137553428194",
      "5893526156359328",
      "4508252809880749",
      "4917534017431729",
      "4175003741452762",
      "6386447797315201",
      "6393558677408279",
      "6374941093581742"
    ).map(CreditCard)
    val (valid, invalid) = moreFake.partition(_.isValid)
    if (invalid.nonEmpty) {
      println()
      invalid foreach println
    }

    println()

    println("Puedes ejecutar y saber si es un numero de tarjeta valido" +
      " desde linea de comando: Day1/run 5893526156359328")

  }
}
