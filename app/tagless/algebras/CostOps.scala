package tagless.algebras

import java.util.UUID

import entities.{Cost, SponsorInitiatedRequest}

trait CostOps[F[_]] {
  def associateCostToInvoice(costId: UUID, invoiceId: UUID): F[Cost]
  def disassociateCostFromInvoice(costId: UUID): F[Cost]
}
