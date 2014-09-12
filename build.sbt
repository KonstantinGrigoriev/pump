name := "Pump"

organization := "kg"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "io.spray" % "spray-client" % "1.3.1",
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
  "org.clapper" %% "grizzled-slf4j" % "1.0.1",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "junit" % "junit" % "4.11" % "test",
  "org.specs2" %% "specs2" % "2.2.3" % "test",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)
