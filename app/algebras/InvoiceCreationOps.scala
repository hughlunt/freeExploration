package algebras

import java.util.UUID

import cats.InjectK
import cats.free.Free
import domain.Entities._

object InvoiceCreationOps {

  case class SiteInitiatedRequest(id: UUID)

  case class SponsorInitiatedRequest(id: UUID)

  sealed trait NewInvoiceRequestAlg[T]
  final case class TransformSiteInitiatedRequest(request: SiteInitiatedRequest) extends NewInvoiceRequestAlg[Either[Error, Invoice]]
  final case class TransformSponsorInitiatedRequest(request: SponsorInitiatedRequest) extends NewInvoiceRequestAlg[Either[Error, Invoice]]

  type InvoiceCreation[T] = Free[NewInvoiceRequestAlg, T]

  def transformSiteInitiatedRequest(request: SiteInitiatedRequest): InvoiceCreation[Either[Error, Invoice]] =
    Free.liftF(TransformSiteInitiatedRequest(request))

  def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): InvoiceCreation[Either[Error, Invoice]] =
    Free.liftF(TransformSponsorInitiatedRequest(request))

  // Allow composition
  class TransformsInvoiceRequest[F[_]](implicit I: InjectK[NewInvoiceRequestAlg, F]) {
    def transformSiteInitiatedRequest(request: SiteInitiatedRequest): Free[F, Either[Error, Invoice]] =
      Free.inject[NewInvoiceRequestAlg, F](TransformSiteInitiatedRequest(request))
    def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): Free[F, Either[Error, Invoice]] =
      Free.inject[NewInvoiceRequestAlg, F](TransformSponsorInitiatedRequest(request))
  }

  object TransformsInvoiceRequest {
    implicit def transformsInvoiceRequest[F[_]](implicit I: InjectK[NewInvoiceRequestAlg, F]): TransformsInvoiceRequest[F] = new TransformsInvoiceRequest[F]
  }

}