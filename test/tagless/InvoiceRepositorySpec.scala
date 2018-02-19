package tagless

import java.util.UUID

import cats.Id
import entities.Entities.{Draft, Invoice}
import tagless.algebras.InvoiceRepositoryOps
import org.scalatest.{FlatSpec, Matchers}
import tagless.programs.InvoiceRepository

class InvoiceRepositorySpec extends FlatSpec with Matchers {

  val algebra = new InvoiceRepositoryOps[Id] {
    override def fetchInvoice(id: UUID): Id[Invoice] = dummyInvoice
    override def addInvoice(invoice: Invoice): Id[Unit] = ()
  }

  val programs = new InvoiceRepository(algebra)

  it should "fetch an invoice given an id" in {
    programs.fetchInvoiceProgram(dummyInvoice.id) shouldBe dummyInvoice
  }

  it should "write an invoice to the database" in {
    programs.writeInvoiceProgram(dummyInvoice) shouldBe(() : Unit)
  }

  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)
}
