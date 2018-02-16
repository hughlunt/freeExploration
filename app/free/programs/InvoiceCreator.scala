package free.programs

import free.algebras.ComposedTypes.CreatableInvoice
import free.algebras.InvoiceCreationOps._
import free.algebras.InvoiceRepositoryOps.Invoices
import cats.free.Free
import domain.Entities._

object InvoiceCreator {

  def createSiteInitiatedInvoiceProgram(request: SiteInitiatedRequest)
                                       (implicit ci: TransformsInvoiceRequest[CreatableInvoice]): Free[CreatableInvoice, Invoice] =
    createInvoice(ci.transformSiteInitiatedRequest)(request)

  def createSponsorInitiatedInvoiceProgram(request: SponsorInitiatedRequest)
                                          (implicit ci: TransformsInvoiceRequest[CreatableInvoice]): Free[CreatableInvoice, Invoice] =
    createInvoice(ci.transformSponsorInitiatedRequest)(request)


  private def createInvoice[A](f: A => Free[CreatableInvoice, Invoice])(request: A)(implicit i: Invoices[CreatableInvoice]): Free[CreatableInvoice, Invoice] =
    for {
      invoice <- f(request)
      _ <- i.addInvoice(invoice)
    } yield invoice

}
