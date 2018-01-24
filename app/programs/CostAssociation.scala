package programs

import java.util.UUID

import algebras.ComposedTypes.AssociableCost
import algebras.CostOps.{ChangesCosts, NewCostRequest}
import algebras.CostRepositoryOps.Costs
import algebras.InvoiceRepositoryOps.Invoices
import cats.free.Free
import domain.Entities._

object CostAssociation {

  def createAndAssociateCost(costRequest: NewCostRequest, invoiceId: UUID)
                (implicit cc: ChangesCosts[AssociableCost], i: Invoices[AssociableCost], c: Costs[AssociableCost]): Free[AssociableCost, Either[Error, Cost]] = {
    import i._, c._

    cc.transformNewCostRequest(costRequest).flatMap {
      case Left(error) => Free.pure(Left(error))
      case Right(cost) =>
        for {
          _ <- fetchInvoice(invoiceId)
          _ <- addCost(cost)
          _ <- cc.associateCostToInvoice(cost.id, invoiceId)
          _ <- addCost(cost)
        } yield Right(cost)
    }
  }

  def associateCost(costId: UUID, invoiceId: UUID)(implicit cc: ChangesCosts[AssociableCost], i: Invoices[AssociableCost], c: Costs[AssociableCost]): Free[AssociableCost, Either[Error, Cost]] = {
    import i._, c._

    for {
      _ <- fetchInvoice(invoiceId)
      _ <- fetchCost(costId)
      possibleAssociatedCost <- cc.associateCostToInvoice(costId, invoiceId)
      associatedCost <- possibleAssociatedCost
      _ <- addCost(associatedCost)
    } yield Right(associatedCost)
  }
}
