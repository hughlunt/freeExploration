package free.programs

import free.algebras.ComposedTypes.CreatableCost
import free.algebras.CostOps.{ChangesCosts, NewCostRequest}
import free.algebras.CostRepositoryOps.Costs
import cats.free.Free
import entities._

object CostCreation {

  def createCost(request: NewCostRequest)
                (implicit cc: ChangesCosts[CreatableCost], c: Costs[CreatableCost]): Free[CreatableCost, Cost] = {
    import c._

    for {
      cost <- cc.transformNewCostRequest(request)
      _ <- addCost(cost)
    } yield cost
  }
}
