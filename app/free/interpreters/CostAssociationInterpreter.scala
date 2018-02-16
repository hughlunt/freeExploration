package free.interpreters

import java.util.UUID

import free.algebras.CostOps.{AssociateCostToInvoice, CostAlg, DisassociateCostFromInvoice, TransformNewCostRequest}
import cats.data.EitherT
import cats.~>
import cats.implicits._
import com.google.inject.Singleton
import entities.Entities.Cost
import entities.HelperTypes.FEither

import scala.concurrent.{ExecutionContext, Future}

class CostAssociationInterpreter(implicit val ec: ExecutionContext) {
  val costAssociationInterpreter = new (CostAlg ~> FEither) {
    override def apply[A](fa: CostAlg[A]): FEither[A] = fa match {
      case TransformNewCostRequest(request) => EitherT.fromEither(Right(Cost(UUID.randomUUID(), 1.5, None)))
      case AssociateCostToInvoice(cost, invoiceId) => EitherT.fromEither(Right(Cost(UUID.randomUUID(), 1.5, Some(UUID.randomUUID()))))
      case DisassociateCostFromInvoice(costId) => EitherT.fromEither(Right(Cost(UUID.randomUUID(), 1.5, None)))
    }
  }
}

