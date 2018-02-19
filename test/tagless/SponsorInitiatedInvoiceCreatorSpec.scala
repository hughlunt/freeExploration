package tagless

import java.util.UUID

import cats.Id
import entities.Entities
import entities.Entities._
import org.scalatest.{FlatSpec, Matchers}
import tagless.algebras.{CostOps, CostRepositoryOps, InvoiceCreationOps, InvoiceRepositoryOps}
import tagless.programs.SponsorInitiatedInvoiceCreator

class SponsorInitiatedInvoiceCreatorSpec extends FlatSpec with Matchers {

  val transformAlgebra: InvoiceCreationOps[Id] = new InvoiceCreationOps[Id] {
    override def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Id[Invoice] = dummyInvoice
    override def transformSponsorInitiatedRequest(request: Entities.SponsorInitiatedRequest): Id[(Invoice, Cost)] = (dummyInvoice, disassociatedCost)
  }

  val invoiceRepoAlgebra: InvoiceRepositoryOps[Id] = new InvoiceRepositoryOps[Id] {
    override def fetchInvoice(id: UUID): Id[Invoice] = ??? // Method not used in this test
    override def addInvoice(invoice: Invoice): Id[Unit] = ()
  }

  val costRepoAlgebra: CostRepositoryOps[Id] = new CostRepositoryOps[Id] {
    override def fetchCost(id: UUID): Id[Cost] = ??? // Method not used in this test
    override def addCost(cost: Cost): Id[Unit] = ()
    override def deleteCost(id: UUID): Id[Unit] = ??? // Method not used in this test
  }

  val costOps: CostOps[Id] = new CostOps[Id] {
    override def associateCostToInvoice(costId: UUID, invoiceId: UUID): Id[Cost] = disassociatedCost.copy(invoiceId = Some(invoiceId))
    override def disassociateCostFromInvoice(costId: UUID): Id[Cost] = ??? // Not used in this test
  }

  val programs = new SponsorInitiatedInvoiceCreator(transformAlgebra, invoiceRepoAlgebra, costRepoAlgebra, costOps)
  it should "Transform a sponsor initiated invoice request" in {
    programs.createSponsorInitiatedInvoiceProgram(sponsorRequest) shouldBe dummyInvoice
  }

  val disassociatedCost = Cost(UUID.randomUUID(), 100.21, None)
  val sponsorRequest = SponsorInitiatedRequest(disassociatedCost, UUID.randomUUID())
  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)

}
