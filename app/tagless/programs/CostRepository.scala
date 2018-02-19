package tagless.programs

import java.util.UUID

import cats.Monad
import entities.Entities.Cost
import tagless.algebras.CostRepositoryOps

class CostRepository[F[_] : Monad](alg: CostRepositoryOps[F]) {
  import alg._

  def addCostProgram(cost: Cost): F[Unit] = addCost(cost)
  def fetchCostProgram(id: UUID): F[Cost] = fetchCost(id)
  def deleteCostProgram(id: UUID): F[Unit] = deleteCost(id)
}
