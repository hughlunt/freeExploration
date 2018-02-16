package tagless.algebras

import java.util.UUID

import entities.Entities.Invoice

trait InvoiceRepositoryOps[F[_]] {
  def addInvoice(invoice: Invoice): F[Unit]
  def fetchInvoice(id: UUID): F[Invoice]
}
