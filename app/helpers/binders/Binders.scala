package helpers.binders

import java.util.UUID

import play.api.mvc.PathBindable

object Binders {
  implicit def uuidPathBindable(implicit stringBinder: PathBindable[String]): PathBindable[UUID] = new PathBindable[UUID] {
    override def bind(key: String, value: String): Either[String, UUID] = {
      stringBinder.bind(key, value).right.flatMap {
        case x if x.length == 36 => Right(UUID.fromString(x))
        case _ => Left("Invalid UUID format")
      }
    }
    override def unbind(key: String, uuid: UUID): String = {
      uuid.toString
    }
  }
}
