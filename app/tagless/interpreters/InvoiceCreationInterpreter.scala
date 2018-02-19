package tagless.interpreters

import java.util.UUID

import cats.data.EitherT
import cats.implicits._
import com.google.inject.Inject
import entities.Entities._
import entities.HelperTypes.FEither
import tagless.algebras.InvoiceCreationOps

import scala.concurrent.ExecutionContext

class InvoiceCreationInterpreter @Inject()(implicit ec: ExecutionContext) extends InvoiceCreationOps[FEither] {
  override def transformSiteInitiatedRequest(request: SiteInitiatedRequest): FEither[Invoice] =
    EitherT.fromEither(Right(Invoice(request.id, AwaitingApproval)))

  override def transformSponsorInitiatedRequest(request: SponsorInitiatedRequest): FEither[(Invoice, Cost)] =
    EitherT.fromEither(Right((Invoice(request.id, Draft), Cost(UUID.randomUUID(), 13.2, None))))
}
