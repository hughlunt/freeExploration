package interpreters

import algebras.InvoiceCreationOps._
import cats.~>
import domain.Entities._

import scala.concurrent.Future

object InvoiceCreationInterpreter {
  val invoiceCreationInterpreter = new (NewInvoiceRequestAlg ~> Future) {
    override def apply[A](fa: NewInvoiceRequestAlg[A]) = fa match {
      case TransformSiteInitiatedRequest(request) => Future.successful(Right(Invoice(request.id, AwaitingApproval)))
      case TransformSponsorInitiatedRequest(request) => Future.successful(Right(Invoice(request.id, Draft)))
    }
  }
}
