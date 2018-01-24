package interpreters

import algebras.InvoiceRepositoryOps.{FetchInvoice, InvoiceRepositoryAlg, AddInvoice}
import cats.~>

import scala.concurrent.Future

object InvoiceRepositoryInterpreter {
  val invoiceRepositoryInterpreter = new (InvoiceRepositoryAlg ~> Future) {
    override def apply[A](fa: InvoiceRepositoryAlg[A]) = fa match {
      case AddInvoice(invoicingProcess) => Future.successful(())
      case FetchInvoice(id) => Future.successful(None)
    }
  }
}
