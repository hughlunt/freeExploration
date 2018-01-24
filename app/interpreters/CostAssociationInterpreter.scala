package interpreters

import java.util.UUID

import algebras.CostOps.{AssociateCostToInvoice, CostAlg, DisassociateCostFromInvoice, TransformNewCostRequest}
import cats.~>
import domain.Entities.Cost

import scala.concurrent.Future

object CostAssociationInterpreter {
  val costAssociationInterpreter = new (CostAlg ~> Future) {
    override def apply[A](fa: CostAlg[A]) = fa match {
      case TransformNewCostRequest(request) => Future.successful(Right(Cost(UUID.randomUUID(), 1.5)))
      case AssociateCostToInvoice(costId, invoiceId) => Future.successful(Right(Cost(UUID.randomUUID(), 1.5)))
      case DisassociateCostFromInvoice(costId, invoiceId) => Future.successful(Right(Cost(UUID.randomUUID(), 1.5)))
    }
  }
}
