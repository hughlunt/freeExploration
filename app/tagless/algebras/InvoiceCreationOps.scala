package tagless.algebras

import domain.Entities.{Invoice, SiteInitiatedRequest}

trait InvoiceCreationOps[F[_]] {
  def transformSiteInitiatedRequest(request: SiteInitiatedRequest): F[Invoice]
}
