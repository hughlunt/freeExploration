package tagless.algebras

import entities.{Cost, Invoice, SiteInitiatedRequest, SponsorInitiatedRequest}

trait InvoiceCreationOps[F[_]] {
  def transformSiteInitiatedRequest(request: SiteInitiatedRequest): F[Invoice]
  def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): F[(Invoice, Cost)]
}
