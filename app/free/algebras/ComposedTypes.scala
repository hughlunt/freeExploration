package free.algebras

import free.algebras.CostOps.CostAlg
import free.algebras.CostRepositoryOps.CostRepositoryAlg
import free.algebras.InvoiceCreationOps.NewInvoiceRequestAlg
import free.algebras.InvoiceRepositoryOps.InvoiceRepositoryAlg
import cats.data.EitherK

object ComposedTypes {
  type CreatableInvoice[T] = EitherK[InvoiceRepositoryAlg, NewInvoiceRequestAlg, T]
  type CreatableCost[T] = EitherK[CostRepositoryAlg, CostAlg, T]
  type AssociableCost[T] = EitherK[InvoiceRepositoryAlg, CreatableCost, T]
}
