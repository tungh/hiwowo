import play.Project._
name := "hiwowo"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)     

playScalaSettings
