package algebras

import java.util.UUID

import cats.InjectK
import cats.free.Free
import domain.Entities.{Cost, Error}

object CostOps {

  case class NewCostRequest(amount: BigDecimal)

  sealed trait CostAlg[T]
  final case class TransformNewCostRequest(request: NewCostRequest) extends CostAlg[Either[Error, Cost]]
  final case class AssociateCostToInvoice(costId: UUID, invoiceId: UUID) extends CostAlg[Either[Error, Cost]]
  final case class DisassociateCostFromInvoice(costId: UUID, invoiceId: UUID) extends CostAlg[Either[Error, Cost]]

  class ChangesCosts[F[_]](implicit I: InjectK[CostAlg, F]) {
    def transformNewCostRequest(request: NewCostRequest): Free[F, Either[Error, Cost]] =
      Free.inject[CostAlg, F](TransformNewCostRequest(request))

    def associateCostToInvoice(costId: UUID, invoiceId: UUID): Free[F, Either[Error, Cost]] =
      Free.inject[CostAlg, F](AssociateCostToInvoice(costId, invoiceId))

    def disassociateCostFromInvoice(costId: UUID, invoiceId: UUID): Free[F, Either[Error, Cost]] =
      Free.inject[CostAlg, F](DisassociateCostFromInvoice(costId, invoiceId))
  }
}