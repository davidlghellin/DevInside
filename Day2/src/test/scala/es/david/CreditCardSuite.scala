package es.david

import org.scalatest.{FunSuite, Matchers}

class CreditCardSuite extends FunSuite with Matchers {
  test("Creating a card without passing any number should generate a valid card") {
    CreditCard().isValid shouldBe true
    // Como el metodo empieza por is... podemos hacer uso de la siguiente notacion
    CreditCard() shouldBe 'valid
  }
  test("Creating a card without passing any number should create a card of class CreditCard.Valid") {
    CreditCard() shouldBe a[CreditCard.Valid]
    CreditCard() should not be a[CreditCard.Invalid]
    CreditCard().isInstanceOf[CreditCard.Valid] shouldBe true
  }

  test("Creating a card manually by passing a valid number should produce a valid credit card") {
    val validNumber = CreditCard().number
    CreditCard(validNumber).isValid shouldBe true

    noException should be thrownBy CreditCard(validNumber).asInstanceOf[CreditCard.Valid]
  }

  test("Credit card´s toString method should mention validaty") {
    CreditCard("").toString.toLowerCase should include("invalid")
    CreditCard().toString.toLowerCase should not include "invalid"
  }

  test("All these numbers should be valid") {
    val fakeCards = Seq(
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

    all(fakeCards.map(_.isValid)) shouldBe true
  }
  test("10k generated numbers should all be valid") {
    val fakeCards = 1 to 10000 map (_ => CreditCard())
    all(fakeCards.map(_.isValid)) shouldBe true
  }
}
