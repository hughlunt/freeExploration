package programs

import java.util.UUID

import algebras.InvoiceRepositoryOps._
import domain.Entities.Invoice

object InvoiceRepository {

  def writeInvoiceProgram(invoicingProcess: Invoice): InvoiceRepository[Unit] = addInvoice(invoicingProcess)

  def fetchInvoiceProgram(id: UUID): InvoiceRepository[Option[Invoice]] = fetchInvoice(id)
}
