package tagless.programs

import java.util.UUID

import cats.Monad
import domain.Entities.Invoice
import tagless.algebras.InvoiceRepositoryOps

class InvoiceRepository[F[_] : Monad](alg: InvoiceRepositoryOps[F]) {
  import alg._

  def writeInvoiceProgram(invoice: Invoice): F[Unit] = addInvoice(invoice)

  def fetchInvoice(id: UUID): F[Invoice] = alg.fetchInvoice(id)
}
