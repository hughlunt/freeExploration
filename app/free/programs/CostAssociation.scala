package free.programs

import java.util.UUID

import free.algebras.ComposedTypes.AssociableCost
import free.algebras.CostOps.{ChangesCosts, NewCostRequest}
import free.algebras.CostRepositoryOps.Costs
import free.algebras.InvoiceRepositoryOps.Invoices
import cats.free.Free
import domain.Entities._

object CostAssociation {

  def createAndAssociateCost(costRequest: NewCostRequest, invoiceId: UUID)
                (implicit cc: ChangesCosts[AssociableCost], c: Costs[AssociableCost]): Free[AssociableCost, Cost] = {
    import c._

    for {
      cost <- cc.transformNewCostRequest(costRequest)
      _ <- addCost(cost)
      associatedCost <- associateCost(cost.id, invoiceId)
    } yield associatedCost
  }

  def associateCost(costId: UUID, invoiceId: UUID)(implicit cc: ChangesCosts[AssociableCost], i: Invoices[AssociableCost], c: Costs[AssociableCost]): Free[AssociableCost, Cost] = {
    import i._, c._

    for {
      cost <- fetchCost(costId)
      _ <- fetchInvoice(invoiceId)
      associatedCost <- cc.associateCostToInvoice(cost, invoiceId)
      _ <- updateCost(associatedCost)
    } yield associatedCost
  }

  def disassociateCost(costId: UUID)(implicit cc: ChangesCosts[AssociableCost], c: Costs[AssociableCost]): Free[AssociableCost, Cost] = {
    import c._

    for {
      cost <- fetchCost(costId)
      disassociatedCost <- cc.disassociateCostFromInvoice(costId)
      _ <- updateCost(disassociatedCost)
    } yield disassociatedCost
  }
}
