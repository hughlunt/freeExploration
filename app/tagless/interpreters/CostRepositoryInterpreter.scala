package tagless.interpreters

import java.util.UUID

import cats.data.EitherT
import cats.implicits._
import com.google.inject.Inject
import entities._
import entities.HelperTypes.FEither
import tagless.algebras.CostRepositoryOps

import scala.concurrent.{ExecutionContext, Future}

class CostRepositoryInterpreter @Inject()(implicit ec: ExecutionContext) extends CostRepositoryOps[FEither] {
  override def addCost(cost: Cost): FEither[Unit] = EitherT.right(Future.successful(()))

  override def fetchCost(id: UUID): FEither[Cost] = EitherT.right(Future.successful(dummyCost))

  override def deleteCost(id: UUID): FEither[Unit] = EitherT.right(Future.successful(()))

  val dummyCost = Cost(UUID.randomUUID(), 100.21, None)
}
