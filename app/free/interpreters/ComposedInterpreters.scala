package free.interpreters

import javax.inject.Singleton

import scala.concurrent.ExecutionContext

@Singleton
class ComposedInterpreters(implicit ec: ExecutionContext) {
  val futureSavedInvoiceOrInvoiceInterpreter = new InvoiceRepositoryInterpreter().invoiceRepositoryInterpreter or new InvoiceCreationInterpreter().invoiceCreationInterpreter
  val futureAssociatedCostInterpreter = new InvoiceRepositoryInterpreter().invoiceRepositoryInterpreter or new CostAssociationInterpreter().costAssociationInterpreter
}

