name := "Pump"

organization := "kg"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "net.databinder.dispatch" %% "dispatch-tagsoup" % "0.11.0",
  "org.clapper" %% "grizzled-slf4j" % "1.0.1",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "junit" % "junit" % "4.11" % "test",
  "org.specs2" %% "specs2" % "2.2.3" % "test",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)
