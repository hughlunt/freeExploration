package controllers.tagless

import cats.implicits._
import com.google.inject.{Inject, Singleton}
import entities._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc._
import tagless.interpreters.{CostAssociationInterpreter => TaglessCostAssociationInterpreter, CostRepositoryInterpreter => TaglessCostRepositoryInterpreter, InvoiceCreationInterpreter => TaglessInvoiceCreationInterpreter, InvoiceRepositoryInterpreter => TaglessInvoiceRepositoryInterpreter}
import tagless.programs.{SiteInitiatedInvoiceCreator, SponsorInitiatedInvoiceCreator}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceCreationController @Inject()(val cc: ControllerComponents,
                                          tici: TaglessInvoiceCreationInterpreter,
                                          tiri: TaglessInvoiceRepositoryInterpreter,
                                          tcri: TaglessCostRepositoryInterpreter,
                                          tcci: TaglessCostAssociationInterpreter,
                                          implicit val ec: ExecutionContext
                                         ) extends AbstractController(cc) with Circe {

  def siteInitiated = Action.async(circe.json[SiteInitiatedRequest]) { implicit request =>
    new SiteInitiatedInvoiceCreator(tici, tiri).createSiteInitiatedInvoiceProgram(request.body).fold(e => Ok(e.asJson), ip => Ok(ip.asJson))
      .recoverWith {
        case e => Future.successful(InternalServerError(e.getMessage))
      }
  }

  def sponsorInitiated = Action.async(circe.json[SponsorInitiatedRequest]) { implicit request =>
    new SponsorInitiatedInvoiceCreator(tici, tiri, tcri, tcci).createSponsorInitiatedInvoiceProgram(request.body).fold(e => Ok(e.asJson), ip => Ok(ip.asJson))
      .recoverWith {
        case e => Future.successful(InternalServerError(e.getMessage))
      }
  }
}
