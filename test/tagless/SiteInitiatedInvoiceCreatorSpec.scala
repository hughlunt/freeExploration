package tagless

import java.util.UUID

import cats.Id
import cats.data.Writer
import cats.implicits._
import entities.InvoiceStatus.Draft
import entities._
import org.scalatest.{FlatSpec, Matchers}
import tagless.algebras.{InvoiceCreationOps, InvoiceRepositoryOps}
import tagless.programs.SiteInitiatedInvoiceCreator

class SiteInitiatedInvoiceCreatorSpec extends FlatSpec with Matchers {

  type Log[A] = Writer[List[String], A]

  val transformAlgebra: InvoiceCreationOps[Id] = new InvoiceCreationOps[Id] {
    override def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Id[Invoice] = dummyInvoice
    override def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): Id[(Invoice, Cost)] = ??? // Not used in this test
  }

  val repoAlgebra: InvoiceRepositoryOps[Id] = new InvoiceRepositoryOps[Id] {
    override def fetchInvoice(id: UUID): Id[Invoice] = ??? // Method not used in this test
    override def addInvoice(invoice: Invoice): Id[Unit] = ()
  }

  val programs = new SiteInitiatedInvoiceCreator(transformAlgebra, repoAlgebra)

  it should "Transform a site initiated invoice request" in {
    programs.createSiteInitiatedInvoiceProgram(siteRequest) shouldBe dummyInvoice
  }

  it should "Call the correct functions" in {
    val transformWithLogAlg: InvoiceCreationOps[Log] = new InvoiceCreationOps[Log] {
      override def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Log[Invoice] = Writer(List("Transforming Invoice Request"), dummyInvoice)
      override def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): Log[(Invoice, Cost)] = ??? // Not used in this test
    }

    val repoWithLogAlg: InvoiceRepositoryOps[Log] = new InvoiceRepositoryOps[Log] {
      override def fetchInvoice(id: UUID): Log[Invoice] = ??? // Method not used in this test
      override def addInvoice(invoice: Invoice): Log[Unit] = Writer(List("Adding invoice to db"), ())
    }

    val programsWithLog = new SiteInitiatedInvoiceCreator(transformWithLogAlg, repoWithLogAlg)

    val (logs, result) = programsWithLog.createSiteInitiatedInvoiceProgram(siteRequest).run

    result shouldBe dummyInvoice
    logs shouldBe List("Transforming Invoice Request", "Adding invoice to db")

  }
  val siteRequest = SiteInitiatedRequest(UUID.randomUUID())
  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)
}
