package domain

import java.util.UUID

import io.circe.{Decoder, ObjectEncoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object Entities {
  case class Invoice(id: UUID, status: InvoiceStatus)
  case class Cost(id: UUID, value: BigDecimal)
  case class Error (msg: String) extends AnyVal

  sealed trait InvoiceStatus
  case object Draft extends InvoiceStatus
  case object AwaitingApproval extends InvoiceStatus

  object InvoiceStatus {
    implicit val encodeStatus: ObjectEncoder[InvoiceStatus] = deriveEncoder
    implicit val decodeStatus: Decoder[InvoiceStatus] = deriveDecoder
  }
}
