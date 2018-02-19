package tagless.algebras

import java.util.UUID

import entities.Entities.Cost

trait CostRepositoryOps[F[_]] {
  def addCost(cost: Cost): F[Unit]
  def fetchCost(id: UUID): F[Cost]
  def deleteCost(id: UUID): F[Unit]
}
