import sbt._
import Keys._

import play.Project._

object ApplicationBuild extends Build {

  val appName         = "hiwowo"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc, cache
    ,"mysql" % "mysql-connector-java" % "5.1.21"
    ,"com.typesafe.slick" %% "slick" % "1.0.1"
    ,"org.jsoup" % "jsoup" % "1.7.1"
    ,"net.coobird" % "thumbnailator" % "0.4.4"
    //   , "com.typesafe" %% "play-plugins-mailer" % "2.1-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )
}
            
