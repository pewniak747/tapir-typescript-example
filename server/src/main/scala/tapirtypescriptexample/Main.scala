package tapirtypescriptexample

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import java.util.Properties
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import sttp.tapir._
import io.circe.generic.auto._
import sttp.tapir.docs.openapi.TapirOpenAPIDocs
import sttp.tapir.json.circe._
import sttp.tapir.openapi.circe.yaml.TapirOpenAPICirceYaml
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.akkahttp._

object Main extends App with TapirOpenAPIDocs with TapirOpenAPICirceYaml with Directives {
  implicit lazy val system: ActorSystem  = ActorSystem()
  implicit lazy val ec: ExecutionContext = system.dispatcher

  // Tapir endpoint definitions
  case class Widget(id: Long, name: String, description: Option[String])
  case class WidgetsGetSuccess(widgets: Seq[Widget])
  case class WidgetCreate(name: String, description: Option[String])
  case class WidgetCreateSuccess(id: Long)

  val getWidgetsEndpoint =
    endpoint.get
      .in("widgets")
      .description("Get all widgets")
      .name("GetWidgets")
      .tag("widgets")
      .out(jsonBody[WidgetsGetSuccess])
      .serverLogic { _ =>
        Future({
          val widgets = List(
            Widget(id = 1, name = "ABC", description = None),
            Widget(id = 2, name = "DEF", description = Some("Description"))
          )
          Right(WidgetsGetSuccess(widgets)): Either[Unit, WidgetsGetSuccess]
        })
      }
  val createWidgetEndpoint =
    endpoint.post
      .in("widgets")
      .description("Create a new widget")
      .name("CreateWidget")
      .tag("widgets")
      .in(jsonBody[WidgetCreate])
      .out(jsonBody[WidgetCreateSuccess])
      .serverLogic { (create) =>
        Future({
          println(s"Creating widget: ${create.name}")
          Right(WidgetCreateSuccess(id = 3)): Either[Unit, WidgetCreateSuccess]
        })
      }
  val endpoints = List(getWidgetsEndpoint, createWidgetEndpoint)

  // Swagger API documentation
  lazy val openApiV1Yaml: String = endpoints.toOpenAPI("Tapir + TypeScript Example", "1").toYaml
  val swaggerYml                 = "swagger.yml"
  val swaggerVersion = {
    val p = new Properties()
    p.load(getClass.getResourceAsStream("/META-INF/maven/org.webjars/swagger-ui/pom.properties"))
    p.getProperty("version")
  }
  val redirectToIndex: Route =
    redirect(s"/apidoc/index.html?url=/apidoc/$swaggerYml", StatusCodes.PermanentRedirect)

  val apidoc: Route =
    path("apidoc") {
      redirectToIndex
    } ~
      pathPrefix("apidoc") {
        path("") {
          redirectToIndex
        } ~
          path(swaggerYml) {
            complete(openApiV1Yaml)
          } ~
          getFromResourceDirectory(s"META-INF/resources/webjars/swagger-ui/$swaggerVersion/")
      }

  // Run HTTP server
  val httpInterface = "localhost"
  val httpPort      = 8080
  val routes        = endpoints.toRoute ~ apidoc

  println(s"Visit http://$httpInterface:$httpPort/apidoc to see Swagger API documentation")
  Http().bindAndHandle(routes, httpInterface, httpPort)
}
