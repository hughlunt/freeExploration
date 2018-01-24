package programs

import algebras.ComposedTypes.CreatableCost
import algebras.CostOps.{ChangesCosts, NewCostRequest}
import algebras.CostRepositoryOps.Costs
import cats.free.Free
import domain.Entities._

object CostCreation {

  def createCost(request: NewCostRequest)
                (implicit cc: ChangesCosts[CreatableCost], c: Costs[CreatableCost]): Free[CreatableCost, Either[Error, Cost]] = {
    import c._

    cc.transformNewCostRequest(request).flatMap {
      case Left(error) => Free.pure(Left(error))
      case Right(cost) =>
        for {
          _ <- addCost(cost)
        } yield Right(cost)
    }
  }
}
