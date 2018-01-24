package controllers

import algebras.ComposedTypes.CreatableInvoice
import cats.implicits._
import com.google.inject.{Inject, Singleton}
import algebras.InvoiceCreationOps.{SiteInitiatedRequest, SponsorInitiatedRequest}
import cats.free.Free
import domain.Entities._
import interpreters.ComposedInterpreters._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc._
import programs.InvoiceCreator._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceCreationController @Inject()(val cc: ControllerComponents,
                                          implicit val ec: ExecutionContext
                                         ) extends AbstractController(cc) with Circe {

  def siteInitiated = Action.async(circe.json[SiteInitiatedRequest]) { implicit request =>
    createInvoice(request.body)(createSiteInitiatedInvoiceProgram)
  }

  def sponsorInitiated = Action.async(circe.json[SponsorInitiatedRequest]) { implicit request =>
    createInvoice(request.body)(createSponsorInitiatedInvoiceProgram)
  }

  private def createInvoice[A](request: A)(f: A => Free[CreatableInvoice, Either[Error, Invoice]]): Future[Result] =
    f(request).foldMap(futureSavedInvoiceOrInvoiceInterpreter).map {
      _.fold(e => Ok(e.asJson), ip => Ok(ip.asJson))
    }.recoverWith {
      case e => Future.successful(InternalServerError(e.getMessage))
    }
}
