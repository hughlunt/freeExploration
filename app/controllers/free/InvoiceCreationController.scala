package controllers.free

import cats.implicits._
import com.google.inject.{Inject, Singleton}
import entities._
import free.interpreters.ComposedInterpreters
import free.programs.InvoiceCreator._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceCreationController @Inject()(val cc: ControllerComponents,
                                          ci: ComposedInterpreters,
                                          implicit val ec: ExecutionContext
                                         ) extends AbstractController(cc) with Circe {

  def siteInitiated = Action.async(circe.json[SiteInitiatedRequest]) { implicit request =>
    createSiteInitiatedInvoiceProgram(request.body).foldMap(ci.futureSavedInvoiceOrInvoiceInterpreter).value.map {
      _.fold(e => Ok(e.asJson), ip => Ok(ip.asJson))
    }.recoverWith {
      case e => Future.successful(InternalServerError(e.getMessage))
    }
  }

  def sponsorInitiated = Action.async(circe.json[SponsorInitiatedRequest]) { implicit request =>
    createSponsorInitiatedInvoiceProgram(request.body)
      .foldMap(ci.futureSavedInvoiceOrInvoiceInterpreter).value.map {
      _.fold(e => Ok(e.asJson), ip => Ok(ip.asJson))
    }.recoverWith {
      case e => Future.successful(InternalServerError(e.getMessage))
    }
  }
}
