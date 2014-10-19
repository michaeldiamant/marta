import sbt._
import Keys._

object MartaBuild extends Build {

  val root =
    Project(
      id = "marta",
      base = file("."),
      settings =
          Seq(
            scalaVersion := "2.11.2",
            scalacOptions ++=
              Seq(
                "-unchecked",
                "-deprecation",
                "-feature",
                "-Xlint",
                "-language:reflectiveCalls"),
            testOptions in Test := Seq(Tests.Filter(s => s.endsWith("Spec"))),
            resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
            libraryDependencies ++=
              Seq(
                "commons-io" % "commons-io" % "2.4",
                "joda-time" % "joda-time" % "2.5",
                "org.joda" % "joda-convert" % "1.7",
                "org.scalatest" %% "scalatest" % "2.2.2" % "test",
                "junit" % "junit" % "4.11" % "test",
                "org.mockito" % "mockito-all" % "1.10.8" % "test",
                "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
                "org.specs2" %% "specs2" % "2.4.6" % "test",
                "org.scalacheck" %% "scalacheck" % "1.11.6" % "test")))
}
