package free.interpreters

import java.util.UUID

import free.algebras.InvoiceRepositoryOps.{AddInvoice, FetchInvoice, InvoiceRepositoryAlg}
import cats.data.EitherT
import cats.~>
import cats.implicits._
import com.google.inject.{Inject, Singleton}
import entities.Invoice
import entities.HelperTypes.FEither
import entities.InvoiceStatus.Draft

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceRepositoryInterpreter @Inject()(implicit ec: ExecutionContext) {

  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)
  val invoiceRepositoryInterpreter = new (InvoiceRepositoryAlg ~> FEither) {
    override def apply[A](fa: InvoiceRepositoryAlg[A]): FEither[A] = fa match {
      case AddInvoice(invoicingProcess) => EitherT.right(Future.successful(()))
      case FetchInvoice(id) => EitherT.right(Future.successful(dummyInvoice))
    }
  }
}
