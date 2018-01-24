package programs

import algebras.ComposedTypes.CreatableInvoice
import algebras.InvoiceCreationOps._
import algebras.InvoiceRepositoryOps.Invoices
import cats.free.Free
import domain.Entities._

object InvoiceCreator {

  def createSiteInitiatedInvoiceProgram(request: SiteInitiatedRequest)
                                       (implicit ci: TransformsInvoiceRequest[CreatableInvoice]): Free[CreatableInvoice, Either[Error, Invoice]] =
    ci.transformSiteInitiatedRequest(request).flatMap(freeErrorOrInvoice)

  def createSponsorInitiatedInvoiceProgram(request: SponsorInitiatedRequest)
                                          (implicit ci: TransformsInvoiceRequest[CreatableInvoice]): Free[CreatableInvoice, Either[Error, Invoice]] =
    ci.transformSponsorInitiatedRequest(request).flatMap(freeErrorOrInvoice)

  private def freeErrorOrInvoice(transformResult: Either[Error,Invoice])
                                (implicit i: Invoices[CreatableInvoice]): Free[CreatableInvoice, Either[Error, Invoice]] = {
    import i._

    transformResult match {
      case Left(error)    => Free.pure(Left(error))
      case Right(invoice) =>
        for {
          _ <- addInvoice(invoice)
        } yield Right(invoice)
    }
  }
}
