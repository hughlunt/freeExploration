package free.programs

import java.util.UUID

import free.algebras.InvoiceRepositoryOps._
import entities.Invoice

object InvoiceRepository {

  def writeInvoiceProgram(invoicingProcess: Invoice): InvoiceRepository[Unit] = addInvoice(invoicingProcess)

  def fetchInvoiceProgram(id: UUID): InvoiceRepository[Invoice] = fetchInvoice(id)
}
