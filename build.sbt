name := "pump"

organization := "kg"

version := "0.1.0-SNAPSHOT"

sbtVersion := "0.13.6"

scalaVersion := "2.11.2"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "io.spray" %% "spray-client" % "1.3.1",
    "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.scalatest" %% "scalatest" % "2.2.2" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
  )
}

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Ywarn-dead-code",
  "-Yrangepos",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

resolvers += "spray repo" at "http://repo.spray.io"
