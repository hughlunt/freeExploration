package tagless.programs

import cats.implicits._
import cats.Monad
import domain.Entities.{Invoice, SiteInitiatedRequest}
import tagless.algebras.{InvoiceCreationOps, InvoiceRepositoryOps}

class InvoiceCreator[F[_] : Monad](
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
