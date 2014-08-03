

name := """hiwowo"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc, cache
    ,"mysql" % "mysql-connector-java" % "5.1.9"
    ,"org.jsoup" % "jsoup" % "1.7.1"
    ,"com.typesafe.play" %% "play-slick" % "0.8.0-RC3"
    ,"com.sksamuel.scrimage" %% "scrimage-core" % "1.4.1"
    ,"com.sksamuel.scrimage" %% "scrimage-canvas" % "1.4.1"
    ,"com.sksamuel.scrimage" %% "scrimage-filters" % "1.4.1"
    ,"org.apache.httpcomponents" % "httpcomponents-core" % "4.3.1"
    ,"org.apache.httpcomponents" % "httpcomponents-client" % "4.3.1"
)

fork in Test := false

lazy val root = (project in file(".")).enablePlugins(PlayScala)
