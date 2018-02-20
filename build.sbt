name := """free_monad_site_invoices"""
organization := "com.mdsol"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SwaggerPlugin)

scalaVersion := "2.12.3"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.typelevel" %% "cats-free" % "1.0.1",
  "com.dripower" %% "play-circe" % "2609.0",
  "org.webjars" % "swagger-ui" % "3.9.3",
  "com.beachape" %% "enumeratum-circe" % "1.5.15"
)
swaggerV3 := true
swaggerDomainNameSpaces := Seq("entities")

routesImport += "helpers.binders.Binders._"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.mdsol.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.mdsol.binders._"
