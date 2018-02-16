package entities

import cats.data.{EitherT, OptionT}
import entities.Entities.Error

import scala.concurrent.Future

object HelperTypes {

  type FEither[A] = EitherT[Future, Error, A]
  type FutureOfOption[A] = OptionT[Future, A]
}
