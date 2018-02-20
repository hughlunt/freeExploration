package entities

import java.util.UUID
import cats.syntax.either._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import org.scalatest.{FlatSpec, Matchers}

class EntitiesSpec extends FlatSpec with Matchers {

  it should "encode Invoice to json correctly" in {
    Invoice(uuid, InvoiceStatus.Draft).asJson shouldBe parse(expectedInvoice).getOrElse(Json.Null)
  }

  val uuid = UUID.randomUUID()
  val expectedInvoice: String =
    s"""{
      |"id": "${uuid.toString}",
      |"status": "Draft"
      |}
    """.stripMargin
}
