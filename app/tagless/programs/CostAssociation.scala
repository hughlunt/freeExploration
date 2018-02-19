package tagless.programs

import java.util.UUID

import cats.Monad
import entities.Entities.Cost
import tagless.algebras.CostOps

class CostAssociation[F[_] : Monad](alg: CostOps[F]) {
  import alg._

  def associateCostProgram(costId: UUID, invoiceId: UUID): F[Cost] = associateCostToInvoice(costId, invoiceId)
  def disassociateCostProgram(costId: UUID): F[Cost] = disassociateCostFromInvoice(costId)
}
