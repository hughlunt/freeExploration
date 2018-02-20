package controllers.free

import java.util.UUID

import cats.implicits._
import com.google.inject.{Inject, Singleton}
import free.interpreters.InvoiceRepositoryInterpreter
import free.programs.InvoiceRepository
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceRetrievalController @Inject()(cc: ControllerComponents,
                                           iri: InvoiceRepositoryInterpreter,
                                           implicit val ec: ExecutionContext) extends AbstractController(cc) with Circe {

  def getSingleInvoice(id: UUID) = Action.async { request =>
    InvoiceRepository.fetchInvoiceProgram(id).foldMap(iri.invoiceRepositoryInterpreter).value.map {
      _.fold(_ => Ok("Invoice not found"), inv => Ok(inv.asJson))
    }.recoverWith {
      case e => Future.successful(InternalServerError(e.getMessage))
    }
  }
}
