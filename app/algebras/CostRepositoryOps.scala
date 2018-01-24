package algebras

import java.util.UUID

import cats.InjectK
import cats.free.Free
import domain.Entities.Cost

object CostRepositoryOps {

  sealed trait CostRepositoryAlg[T]
  final case class AddCost(cost: Cost) extends CostRepositoryAlg[Unit]
  final case class FetchCost(id: UUID) extends CostRepositoryAlg[Option[Cost]]
  final case class DeleteCost(id: UUID) extends CostRepositoryAlg[Unit]
  final case class UpdateCost(id: UUID)

  // For non-composed
  type CostRepository[T] = Free[CostRepositoryAlg, T]

  def addCost(cost: Cost): CostRepository[Unit] =
    Free.liftF(AddCost(cost))

  def fetchCost(id: UUID): CostRepository[Option[Cost]] =
    Free.liftF(FetchCost(id))

  def deleteCost(id: UUID): CostRepository[Unit] =
    Free.liftF(DeleteCost(id))

  // For composition use
  class Costs[F[_]](implicit I: InjectK[CostRepositoryAlg, F]) {
    def addCost(cost: Cost): Free[F, Unit] =
      Free.inject[CostRepositoryAlg, F](AddCost(cost))

    def fetchCost(id: UUID): Free[F, Option[Cost]] =
      Free.inject[CostRepositoryAlg, F](FetchCost(id))

    def deleteCost(id: UUID): Free[F, Unit] =
      Free.inject[CostRepositoryAlg, F](DeleteCost(id))
  }

  object Costs {
    implicit def costs[F[_]](implicit I: InjectK[CostRepositoryAlg, F]): Costs[F] = new Costs[F]
  }
}
