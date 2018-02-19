package tagless.programs

import cats.implicits._
import cats.Monad
import entities.Entities.{Invoice, SiteInitiatedRequest}
import tagless.algebras.{InvoiceCreationOps, InvoiceRepositoryOps}

class SiteInitiatedInvoiceCreator[F[_] : Monad](
                                    requestTransformationAlg: InvoiceCreationOps[F],
                                    invoiceRepositoryAlg: InvoiceRepositoryOps[F]) {
  import requestTransformationAlg._, invoiceRepositoryAlg._

  def createSiteInitiatedInvoiceProgram(request: SiteInitiatedRequest): F[Invoice] = {
    for {
      invoice <- transformSiteInitiatedRequest(request)
      _ <- addInvoice(invoice)
    } yield invoice
  }
}
