package tagless.interpreters

import java.util.UUID

import cats.data.EitherT
import cats.implicits._
import com.google.inject.Inject
import entities.Entities.Cost
import entities.HelperTypes.FEither
import tagless.algebras.CostOps

import scala.concurrent.ExecutionContext

class CostAssociationInterpreter @Inject()(implicit ec: ExecutionContext) extends CostOps[FEither] {

  override def associateCostToInvoice(costId: UUID, invoiceId: UUID): FEither[Cost] =
    EitherT.fromEither(Right(Cost(UUID.randomUUID(), 1.5, Some(UUID.randomUUID()))))

  override def disassociateCostFromInvoice(costId: UUID): FEither[Cost] =
    EitherT.fromEither(Right(Cost(UUID.randomUUID(), 1.5, None)))
}
