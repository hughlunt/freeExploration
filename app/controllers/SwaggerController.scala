package controllers

import com.google.inject.Inject
import play.api.mvc._

class SwaggerController  @Inject()(val cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Redirect("/docs/swagger-ui/index.html?url=/assets/swagger.json")
  }
}
