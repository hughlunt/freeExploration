package free

import java.util.UUID

import cats.{Id, ~>}
import entities.Entities.{AwaitingApproval, Draft, Invoice, SiteInitiatedRequest}
import free.algebras.InvoiceCreationOps.{NewInvoiceRequestAlg, TransformSiteInitiatedRequest, TransformSponsorInitiatedRequest}
import free.algebras.InvoiceRepositoryOps.{AddInvoice, FetchInvoice, InvoiceRepositoryAlg}
import org.scalatest._

class SiteInitiatedInvoiceCreatorSpec extends FlatSpec with Matchers {
  import free.programs.InvoiceCreator._

  def testTransformInterpreter = new (NewInvoiceRequestAlg ~> Id) {
    override def apply[A](fa: NewInvoiceRequestAlg[A]): Id[A] = fa match {
      case TransformSiteInitiatedRequest(request) => Invoice(request.id, AwaitingApproval)
      case TransformSponsorInitiatedRequest(request) => Invoice(request.id, Draft)
    }
  }

  def testRepoInterpreter = new (InvoiceRepositoryAlg ~> Id) {
    override def apply[A](fa: InvoiceRepositoryAlg[A]): Id[A] = fa match {
      case AddInvoice(invoicingProcess) => ()
      case FetchInvoice(id) => ???
    }
  }

  val testInterpreter = testTransformInterpreter or testRepoInterpreter

//  it should "Transform a site initiated invoice request" in {
//    val request: SiteInitiatedRequest = SiteInitiatedRequest(UUID.randomUUID())
//    createSiteInitiatedInvoiceProgram(request)(implicitly).foldMap(testInterpreter) shouldBe Invoice(request.id, AwaitingApproval)
//  }
}
