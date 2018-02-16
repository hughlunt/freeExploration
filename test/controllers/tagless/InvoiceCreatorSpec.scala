package controllers.tagless

import java.util.UUID

import cats.Id
import entities.Entities.{Draft, Invoice, SiteInitiatedRequest}
import org.scalatest.{FlatSpec, Matchers}
import tagless.algebras.{InvoiceCreationOps, InvoiceRepositoryOps}
import tagless.programs.InvoiceCreator

class InvoiceCreatorSpec extends FlatSpec with Matchers {

  val transformAlgebra = new InvoiceCreationOps[Id] {
    override def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Id[Invoice] = dummyInvoice
  }

  val repoAlgebra = new InvoiceRepositoryOps[Id] {
    override def fetchInvoice(id: UUID): Id[Invoice] = ??? // Method not used in this test
    override def addInvoice(invoice: Invoice): Id[Unit] = ()
  }

  val programs = new InvoiceCreator(transformAlgebra, repoAlgebra)

  it should "Transform a site initiated invoice request" in {
    programs.createSiteInitiatedInvoiceProgram(request) shouldBe dummyInvoice
  }

  val request = SiteInitiatedRequest(UUID.randomUUID())
  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)
}
