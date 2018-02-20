import cats.data.{EitherT, OptionT}

import scala.concurrent.Future
import enumeratum._

package object entities {

  import java.util.UUID

  case class Invoice(id: UUID, status : InvoiceStatus)
  case class Cost(id: UUID, value: BigDecimal, invoiceId: Option[UUID])
  case class Error (msg: String) extends AnyVal

  sealed trait InvoiceStatus extends EnumEntry
  object InvoiceStatus extends Enum[InvoiceStatus] with CirceEnum[InvoiceStatus] {
    val values = findValues

    case object Draft extends InvoiceStatus
    case object AwaitingApproval extends InvoiceStatus
  }

  case class SiteInitiatedRequest(studyId: UUID)
  case class SponsorInitiatedRequest(cost: Cost, studyId: UUID)

  object HelperTypes {
    type FEither[A] = EitherT[Future, Error, A]
    type FutureOfOption[A] = OptionT[Future, A]
  }
}
