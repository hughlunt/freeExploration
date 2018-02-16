package controllers.free

import java.util.UUID

import cats.{Id, ~>}
import cats.implicits._
import entities.Entities.{Draft, Invoice}
import free.algebras.InvoiceRepositoryOps._
import org.scalatest._

class InvoiceRepositorySpec extends FlatSpec with Matchers {

  import free.programs.InvoiceRepository._

  def testInterpreter = new (InvoiceRepositoryAlg ~> Id) {
    override def apply[A](fa: InvoiceRepositoryAlg[A]): Id[A] = fa match {
      case AddInvoice(invoicingProcess) => ()
      case FetchInvoice(id) => dummyInvoice
    }
  }
  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)

  it should "fetch an invoice given an id" in {
    fetchInvoiceProgram(dummyInvoice.id).foldMap(testInterpreter) shouldBe dummyInvoice
  }

  it should "add an invoice" in {
    writeInvoiceProgram(dummyInvoice).foldMap(testInterpreter) shouldBe(() : Unit)
  }
}
