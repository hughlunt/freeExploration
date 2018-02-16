package tagless.algebras

import entities.Entities.{Invoice, SiteInitiatedRequest}

trait InvoiceCreationOps[F[_]] {
  def transformSiteInitiatedRequest(request: SiteInitiatedRequest): F[Invoice]
}
