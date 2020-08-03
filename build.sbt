import sbt._

val akkaVersion     = "2.6.5"
val akkaHttpVersion = "10.1.11"
val circeVersion    = "0.13.0"
val sttpVersion     = "2.2.1"
val tapirVersion    = "0.16.10"

val httpDependencies = Seq(
  "com.typesafe.akka"           %% "akka-actor"             % akkaVersion,
  "com.typesafe.akka"           %% "akka-http"              % akkaHttpVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-core"             % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirVersion
)

val jsonDependencies = Seq(
  "io.circe"                     %% "circe-core"       % circeVersion,
  "io.circe"                     %% "circe-generic"    % circeVersion,
  "io.circe"                     %% "circe-parser"     % circeVersion,
  "com.softwaremill.sttp.client" %% "circe"            % sttpVersion,
  "com.softwaremill.sttp.tapir"  %% "tapir-json-circe" % tapirVersion
)

val apiDocsDependencies = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"         % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml"   % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % tapirVersion
)

lazy val server: Project = (project in file("server"))
  .settings(
    scalaVersion := "2.13.2",
    libraryDependencies ++= httpDependencies ++ jsonDependencies ++ apiDocsDependencies,
    mainClass in Compile := Some("tapirtypescriptexample.Main")
  )
