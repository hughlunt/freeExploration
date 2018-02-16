package tagless.interpreters

import java.util.UUID

import cats.data.EitherT
import cats.implicits._
import com.google.inject.Inject
import entities.Entities.{Draft, Invoice}
import entities.HelperTypes.FEither
import tagless.algebras.InvoiceRepositoryOps

import scala.concurrent.{ExecutionContext, Future}

class InvoiceRepositoryInterpreter @Inject()(implicit ec: ExecutionContext) extends InvoiceRepositoryOps[FEither] {
  override def addInvoice(invoice: Invoice): FEither[Unit] = {
    EitherT.right(Future.successful(()))
  }

  override def fetchInvoice(id: UUID): FEither[Invoice] = {
    EitherT.right(Future.successful(dummyInvoice))
  }
  val dummyInvoice = Invoice(UUID.randomUUID(), Draft)
}
