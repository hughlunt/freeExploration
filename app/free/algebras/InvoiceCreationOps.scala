package free.algebras

import cats.InjectK
import cats.free.Free
import entities._

object InvoiceCreationOps {

  sealed trait NewInvoiceRequestAlg[T]
  final case class TransformSiteInitiatedRequest(request: SiteInitiatedRequest) extends NewInvoiceRequestAlg[Invoice]
  final case class TransformSponsorInitiatedRequest(request: SponsorInitiatedRequest) extends NewInvoiceRequestAlg[Invoice]

  type InvoiceCreation[T] = Free[NewInvoiceRequestAlg, T]

  def transformSiteInitiatedRequest(request: SiteInitiatedRequest): InvoiceCreation[Invoice] =
    Free.liftF(TransformSiteInitiatedRequest(request))

  def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): InvoiceCreation[Invoice] =
    Free.liftF(TransformSponsorInitiatedRequest(request))

  // Allow composition
  class TransformsInvoiceRequest[F[_]](implicit I: InjectK[NewInvoiceRequestAlg, F]) {
    def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Free[F, Invoice] =
      Free.inject[NewInvoiceRequestAlg, F](TransformSiteInitiatedRequest(request))
    def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): Free[F, Invoice] =
      Free.inject[NewInvoiceRequestAlg, F](TransformSponsorInitiatedRequest(request))
  }

  object TransformsInvoiceRequest {
    implicit def transformsInvoiceRequest[F[_]](implicit I: InjectK[NewInvoiceRequestAlg, F]): TransformsInvoiceRequest[F] = new TransformsInvoiceRequest[F]
  }

}