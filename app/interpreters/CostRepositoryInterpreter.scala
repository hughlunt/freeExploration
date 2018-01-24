package interpreters

import algebras.CostRepositoryOps.{AddCost, CostRepositoryAlg, DeleteCost, FetchCost}
import cats.~>

import scala.concurrent.Future
object CostRepositoryInterpreter {
  val costRepositoryInterpreter = new (CostRepositoryAlg ~> Future) {
    override def apply[A](fa: CostRepositoryAlg[A]) = fa match {
      case AddCost(cost) => Future.successful(())
      case FetchCost(id) => Future.successful(None)
      case DeleteCost(id) => Future.successful(())
    }
  }
}
