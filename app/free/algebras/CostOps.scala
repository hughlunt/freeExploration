package free.algebras

import java.util.UUID

import cats.InjectK
import cats.free.Free
import entities.Entities.{Cost, Error}

object CostOps {

  case class NewCostRequest(amount: BigDecimal)

  sealed trait CostAlg[T]
  final case class TransformNewCostRequest(request: NewCostRequest) extends CostAlg[Cost]
  final case class AssociateCostToInvoice(cost: Cost, invoiceId: UUID) extends CostAlg[Cost]
  final case class DisassociateCostFromInvoice(costId: UUID) extends CostAlg[Cost]

  class ChangesCosts[F[_]](implicit I: InjectK[CostAlg, F]) {
    def transformNewCostRequest(request: NewCostRequest): Free[F, Cost] =
      Free.inject[CostAlg, F](TransformNewCostRequest(request))

    def associateCostToInvoice(cost: Cost, invoiceId: UUID): Free[F, Cost] =
      Free.inject[CostAlg, F](AssociateCostToInvoice(cost, invoiceId))

    def disassociateCostFromInvoice(costId: UUID): Free[F, Cost] =
      Free.inject[CostAlg, F](DisassociateCostFromInvoice(costId))
  }
}