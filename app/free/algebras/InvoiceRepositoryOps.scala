package free.algebras

import java.util.UUID

import cats.InjectK
import cats.free.Free
import domain.Entities.Invoice
object InvoiceRepositoryOps {

  sealed trait InvoiceRepositoryAlg[T]
  final case class AddInvoice(invoicingProcess: Invoice) extends InvoiceRepositoryAlg[Unit]
  final case class FetchInvoice(id: UUID) extends InvoiceRepositoryAlg[Invoice]

  // For non-composed
  type InvoiceRepository[T] = Free[InvoiceRepositoryAlg, T]

  def addInvoice(invoicingProcess: Invoice): InvoiceRepository[Unit] =
    Free.liftF(AddInvoice(invoicingProcess))

  def fetchInvoice(id: UUID): InvoiceRepository[Invoice] =
    Free.liftF(FetchInvoice(id))

  // Allow composition
  class Invoices[F[_]](implicit I: InjectK[InvoiceRepositoryAlg, F]) {
    def addInvoice(ip: Invoice): Free[F, Unit] =
      Free.inject[InvoiceRepositoryAlg, F](AddInvoice(ip))
    def fetchInvoice(id: UUID): Free[F, Invoice] =
      Free.inject[InvoiceRepositoryAlg, F](FetchInvoice(id))
  }

  object Invoices {
    implicit def invoices[F[_]](implicit I: InjectK[InvoiceRepositoryAlg, F]): Invoices[F] = new Invoices[F]
  }
}
