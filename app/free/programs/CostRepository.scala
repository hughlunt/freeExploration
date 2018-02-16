package free.programs

import java.util.UUID

import free.algebras.CostRepositoryOps._
import domain.Entities.Cost

object CostRepository {

  def addCostProgram(cost: Cost): CostRepository[Unit] = addCost(cost)

  def fetchCostProgram(id: UUID): CostRepository[Cost] = fetchCost(id)

  def deleteCostProgram(id: UUID): CostRepository[Unit] = deleteCost(id)
}
