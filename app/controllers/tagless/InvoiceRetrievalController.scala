package controllers.tagless

import java.util.UUID

import cats.implicits._
import com.google.inject.{Inject, Singleton}
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, ControllerComponents}
import tagless.interpreters.InvoiceRepositoryInterpreter
import tagless.programs.InvoiceRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceRetrievalController @Inject()(cc: ControllerComponents,
                                           interpreter: InvoiceRepositoryInterpreter,
                                           implicit val ec: ExecutionContext) extends AbstractController(cc) with Circe {

  def getSingleInvoice(invoiceId: UUID) = Action.async { request =>

    new InvoiceRepository(interpreter).fetchInvoiceProgram(invoiceId)
      .fold(_ => Ok("Invoice not found"), inv => Ok(inv.asJson))
      .recoverWith {
        case e => Future.successful(InternalServerError(e.getMessage))
      }
  }
}
