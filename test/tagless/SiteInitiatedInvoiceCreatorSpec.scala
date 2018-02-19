package tagless

import java.util.UUID

import cats.Id
import entities.Entities
import entities.Entities._
import org.scalatest.{FlatSpec, Matchers}
import tagless.algebras.{InvoiceCreationOps, InvoiceRepositoryOps}
import tagless.programs.SiteInitiatedInvoiceCreator

class SiteInitiatedInvoiceCreatorSpec extends FlatSpec with Matchers {

  val transformAlgebra: InvoiceCreationOps[Id] = new InvoiceCreationOps[Id] {
    override def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Id[Invoice] = dummyInvoice
    override def transformSponsorInitiatedRequest(request: Entities.SponsorInitiatedRequest): Id[(Invoice, Cost)] = ??? // Not used in this test
  }

  val repoAlgebra: InvoiceRepositoryOps[Id] = new InvoiceRepositoryOps[Id] {
    override def fetchInvoice(id: UUID): Id[Invoice] = ??? // Method not used in this test
    override def addInvoice(invoice: Invoice): Id[Unit] = ()
  }

  val programs = new SiteInitiatedInvoiceCreator(transformAlgebra, repoAlgebra)

  it should "Transform a site initiated invoice request" in {
    programs.createSiteInitiatedInvoiceProgram(siteRequest) shouldBe dummyInvoice
  }

  val siteRequest = SiteInitiatedRequest(UUID.randomUUID())
  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)
}
