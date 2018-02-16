package free.interpreters

import java.util.UUID

import free.algebras.CostRepositoryOps.{AddCost, CostRepositoryAlg, DeleteCost, FetchCost}
import cats.data._
import cats.~>
import cats.implicits._
import domain.Entities.Cost
import domain.HelperTypes.FEither

import scala.concurrent.{ExecutionContext, Future}

class CostRepositoryInterpreter(implicit ec: ExecutionContext) {

  val dummyCost = Cost(UUID.randomUUID(), 100.21, None)

  val costRepositoryInterpreter = new (CostRepositoryAlg ~> FEither) {
    override def apply[A](fa: CostRepositoryAlg[A]): FEither[A] = fa match {
      case AddCost(cost) => EitherT.right(Future.successful(()))
      case FetchCost(id) => EitherT.right(Future.successful(dummyCost))
      case DeleteCost(id) => EitherT.right(Future.successful(()))
    }
  }
}
