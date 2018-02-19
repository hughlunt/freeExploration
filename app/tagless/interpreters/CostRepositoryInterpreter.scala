package tagless.interpreters

import java.util.UUID

import cats.data.EitherT
import cats.implicits._
import com.google.inject.Inject
import entities.Entities
import entities.Entities.Cost
import entities.HelperTypes.FEither
import tagless.algebras.CostRepositoryOps

import scala.concurrent.{ExecutionContext, Future}

class CostRepositoryInterpreter @Inject()(implicit ec: ExecutionContext) extends CostRepositoryOps[FEither] {
  override def addCost(cost: Entities.Cost): FEither[Unit] = EitherT.right(Future.successful(()))

  override def fetchCost(id: UUID): FEither[Entities.Cost] = EitherT.right(Future.successful(dummyCost))

  override def deleteCost(id: UUID): FEither[Unit] = EitherT.right(Future.successful(()))

  val dummyCost = Cost(UUID.randomUUID(), 100.21, None)
}
