import play.Project._
name := "hiwowo"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
    jdbc, cache
    ,"mysql" % "mysql-connector-java" % "5.1.21"
    ,"com.typesafe.slick" %% "slick" % "2.0.2"
    ,"org.jsoup" % "jsoup" % "1.7.1"
    ,"net.coobird" % "thumbnailator" % "0.4.7"
    ,"com.typesafe.play" %% "play-slick" % "0.6.1"
    ,"com.sksamuel.scrimage" %% "scrimage-core" % "1.4.1"
    ,"com.sksamuel.scrimage" %% "scrimage-canvas" % "1.4.1"
    ,"com.sksamuel.scrimage" %% "scrimage-filters" % "1.4.1"
  )   

playScalaSettings
