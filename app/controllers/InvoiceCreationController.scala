package controllers

import cats.implicits._
import com.google.inject.{Inject, Singleton}
import cats.free.Free
import entities.Entities._
import free.interpreters.ComposedInterpreters
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc._
import free.programs.InvoiceCreator._
import tagless.interpreters.{InvoiceCreationInterpreter => TaglessInvoiceCreationInterpreter,
InvoiceRepositoryInterpreter => TaglessInvoiceRepositoryInterpreter,
CostRepositoryInterpreter => TaglessCostRepositoryInterpreter,
CostAssociationInterpreter => TaglessCostAssociationInterpreter}
import tagless.programs.{SiteInitiatedInvoiceCreator, SponsorInitiatedInvoiceCreator}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceCreationController @Inject()(val cc: ControllerComponents,
                                          ci: ComposedInterpreters,
                                          tici: TaglessInvoiceCreationInterpreter,
                                          tiri: TaglessInvoiceRepositoryInterpreter,
                                          tcri: TaglessCostRepositoryInterpreter,
                                          tcci: TaglessCostAssociationInterpreter,
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

  def taglessSiteInitiated = Action.async(circe.json[SiteInitiatedRequest]) { implicit request =>
    new SiteInitiatedInvoiceCreator(tici, tiri).createSiteInitiatedInvoiceProgram(request.body).fold(e => Ok(e.asJson), ip => Ok(ip.asJson))
      .recoverWith {
      case e => Future.successful(InternalServerError(e.getMessage))
    }
  }

  def taglessSponsorInitiated = Action.async(circe.json[SponsorInitiatedRequest]) { implicit request =>
    new SponsorInitiatedInvoiceCreator(tici, tiri, tcri, tcci).createSponsorInitiatedInvoiceProgram(request.body).fold(e => Ok(e.asJson), ip => Ok(ip.asJson))
      .recoverWith {
        case e => Future.successful(InternalServerError(e.getMessage))
      }
  }
}
