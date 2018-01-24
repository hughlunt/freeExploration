package algebras

import algebras.CostOps.CostAlg
import algebras.CostRepositoryOps.CostRepositoryAlg
import algebras.InvoiceCreationOps.NewInvoiceRequestAlg
import algebras.InvoiceRepositoryOps.InvoiceRepositoryAlg
import cats.data.EitherK

object ComposedTypes {
  type CreatableInvoice[T] = EitherK[InvoiceRepositoryAlg, NewInvoiceRequestAlg, T]
  type CreatableCost[T] = EitherK[CostRepositoryAlg, CostAlg, T]
  type AssociableCost[T] = EitherK[InvoiceRepositoryAlg, CreatableCost, T]
}
