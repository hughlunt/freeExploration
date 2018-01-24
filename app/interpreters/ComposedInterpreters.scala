package interpreters

import InvoiceRepositoryInterpreter._
import InvoiceCreationInterpreter._

object ComposedInterpreters {
  val futureSavedInvoiceOrInvoiceInterpreter = invoiceRepositoryInterpreter or invoiceCreationInterpreter
}
