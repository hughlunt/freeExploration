package tagless

import java.util.UUID

import cats.Id
import entities.InvoiceStatus.Draft
import entities._
import org.scalatest.{FlatSpec, Matchers}
import tagless.algebras.{CostOps, CostRepositoryOps, InvoiceCreationOps, InvoiceRepositoryOps}
import tagless.programs.SponsorInitiatedInvoiceCreator

class SponsorInitiatedInvoiceCreatorSpec extends FlatSpec with Matchers {

  // Test Interpreters
  val transformAlgebra: InvoiceCreationOps[Id] = new InvoiceCreationOps[Id] {
    override def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): Id[(Invoice, Cost)] = (dummyInvoice, disassociatedCost)
    override def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Id[Invoice] = ???
  }

  val invoiceRepoAlgebra: InvoiceRepositoryOps[Id] = new InvoiceRepositoryOps[Id] {
    override def addInvoice(invoice: Invoice): Id[Unit] = ()
    override def fetchInvoice(id: UUID): Id[Invoice] = ??? // Method not used in this test
  }

  val costRepoAlgebra: CostRepositoryOps[Id] = new CostRepositoryOps[Id] {
    override def addCost(cost: Cost): Id[Unit] = ()
    override def fetchCost(id: UUID): Id[Cost] = ??? // Method not used in this test
    override def deleteCost(id: UUID): Id[Unit] = ??? // Method not used in this test
  }

  val costOps: CostOps[Id] = new CostOps[Id] {
    override def associateCostToInvoice(costId: UUID, invoiceId: UUID): Id[Cost] = disassociatedCost.copy(invoiceId = Some(invoiceId))
    override def disassociateCostFromInvoice(costId: UUID): Id[Cost] = ??? // Not used in this test
  }

  // Tests
  it should "Create a sponsor initiated invoice" in {
    val programs = new SponsorInitiatedInvoiceCreator(transformAlgebra, invoiceRepoAlgebra, costRepoAlgebra, costOps)
    programs.createSponsorInitiatedInvoiceProgram(sponsorRequest) shouldBe dummyInvoice
  }

  val disassociatedCost = Cost(UUID.randomUUID(), 100.21, None)
  val sponsorRequest = SponsorInitiatedRequest(disassociatedCost, UUID.randomUUID())
  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)

}
