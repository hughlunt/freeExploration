package free.interpreters

import java.util.UUID

import free.algebras.InvoiceCreationOps._
import cats.data.EitherT
import cats.~>
import cats.implicits._
import entities._
import entities.HelperTypes.FEither
import entities.InvoiceStatus.{AwaitingApproval, Draft}

import scala.concurrent.ExecutionContext

class InvoiceCreationInterpreter(implicit ec: ExecutionContext) {

  val invoiceCreationInterpreter = new (NewInvoiceRequestAlg ~> FEither) {
    override def apply[A](fa: NewInvoiceRequestAlg[A]): FEither[A] = fa match {
      case TransformSiteInitiatedRequest(request) => EitherT.fromEither(Right(Invoice(UUID.randomUUID(), AwaitingApproval)))
      case TransformSponsorInitiatedRequest(request) => EitherT.fromEither(Right(Invoice(UUID.randomUUID(), Draft)))
    }
  }
}
