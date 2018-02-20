package tagless.programs

import java.util.UUID

import cats.Monad
import entities.Invoice
import tagless.algebras.InvoiceRepositoryOps

class InvoiceRepository[F[_] : Monad](alg: InvoiceRepositoryOps[F]) {
  import alg._

  def writeInvoiceProgram(invoice: Invoice): F[Unit] = addInvoice(invoice)

  def fetchInvoiceProgram(id: UUID): F[Invoice] = fetchInvoice(id)
}
