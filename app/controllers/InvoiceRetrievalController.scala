package controllers

import java.util.UUID

import cats.implicits._
import com.google.inject.{Inject, Singleton}
import free.interpreters.InvoiceRepositoryInterpreter
import tagless.interpreters.{InvoiceRepositoryInterpreter => TaglessInvoiceRepositoryInterpreter}
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, ControllerComponents}
import free.programs.InvoiceRepository
import tagless.programs.InvoiceRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceRetrievalController @Inject()(cc: ControllerComponents,
                                           iri: InvoiceRepositoryInterpreter,
                                           tiri: TaglessInvoiceRepositoryInterpreter,
                                           implicit val ec: ExecutionContext) extends AbstractController(cc) with Circe {

  def getSingleInvoice(id: UUID) = Action.async { request =>
    InvoiceRepository.fetchInvoiceProgram(id).foldMap(iri.invoiceRepositoryInterpreter).value.map {
      _.fold(_ => Ok("Invoice not found"), inv => Ok(inv.asJson))
    }.recoverWith {
      case e => Future.successful(InternalServerError(e.getMessage))
    }
  }

  def getTaglessSingleInvoice(id: UUID) = Action.async { request =>
    new InvoiceRepository(tiri).fetchInvoiceProgram(id).fold(_ => Ok("Invoice not found"), inv => Ok(inv.asJson))
      .recoverWith {
      case e => Future.successful(InternalServerError(e.getMessage))
    }
  }
}
