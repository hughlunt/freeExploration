package tagless.programs

import cats.implicits._
import cats.Monad
import entities.Entities.{Invoice, SponsorInitiatedRequest}
import tagless.algebras.{CostRepositoryOps, InvoiceRepositoryOps, CostOps, InvoiceCreationOps}


class SponsorInitiatedInvoiceCreator[F[_] : Monad](
                                                  requestTransformationAlg: InvoiceCreationOps[F],
                                                  invoiceRepositoryAlg: InvoiceRepositoryOps[F],
                                                  costRepositoryAlg: CostRepositoryOps[F],
                                                  costAssociationAlg: CostOps[F]) {
  import requestTransformationAlg._, invoiceRepositoryAlg._, costRepositoryAlg._, costAssociationAlg._


  def createSponsorInitiatedInvoiceProgram(request: SponsorInitiatedRequest): F[Invoice] = {
    for {
      invoiceAndCost <- transformSponsorInitiatedRequest(request)
      _ <- addInvoice(invoiceAndCost._1)
      _ <- addCost(invoiceAndCost._2)
      _ <- associateCostToInvoice(invoiceAndCost._2.id, invoiceAndCost._1.id)
    } yield invoiceAndCost._1
  }
}
