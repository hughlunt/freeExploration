package domain

import cats.data.{EitherT, OptionT}
import domain.Entities.Error

import scala.concurrent.Future

object HelperTypes {

  type FEither[A] = EitherT[Future, Error, A]
  type FutureOfOption[A] = OptionT[Future, A]
}
