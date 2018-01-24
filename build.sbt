name := """free_monad_site_invoices"""
organization := "com.mdsol"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

scalacOptions += "-Ypartial-unification"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.typelevel" %% "cats-free" % "1.0.1"
libraryDependencies += "com.dripower" %% "play-circe" % "2609.0"

routesImport += "play.api.mvc.PathBindable.bindableUUID"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.mdsol.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.mdsol.binders._"
