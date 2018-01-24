package controllers

import java.util.UUID

import cats.implicits._
import com.google.inject.{Inject, Singleton}
import interpreters.InvoiceRepositoryInterpreter._
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, ControllerComponents}
import programs.InvoiceRepository

import scala.concurrent.ExecutionContext

@Singleton
class InvoiceRetrievalController @Inject()(cc: ControllerComponents,
                                           implicit val ec: ExecutionContext) extends AbstractController(cc) with Circe {

  def getSingleInvoice(id: UUID) = Action.async { request =>
    InvoiceRepository.fetchInvoiceProgram(id).foldMap(invoiceRepositoryInterpreter).map {
      _.fold(Ok("Invoice not found"))(inv => Ok(inv.asJson))
    }
  }
}
