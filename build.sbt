name := "DevInsideYou"

lazy val commonSettings = Seq(
  scalacOptions += "-language:_",
  scalacOptions += "-Ypartial-unification",

  //https://scalacenter.github.io/scalafix/docs/rules/RemoveUnused.html
  //scalacOptions += "-Xfatal-warnings",
  // scalacOptions += "-Ywarn-unused-import", // los import que no se usan los detecta

  //https://docs.scala-lang.org/overviews/compiler-options/index.html
  scalacOptions ++= Seq("-encoding", "utf8",
    "-explaintypes"),

  version := "0.1-SNAPSHOT",
  organization := "es.david",
  scalaVersion := "2.12.8"
  //  test in assembly := {}
)
// val scalaTest = "org.scalatest" % "scalatest_2.12" % "3.0.5" % Test
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test
lazy val `DevInsideDavid` = (project in file("."))
  .settings(
    commonSettings)
  .aggregate(`Day1`, `Day2`)
mainClass in Compile := (mainClass in Compile in Day1).value

lazy val `Day1` = (project in file("Day1"))
  .settings(
    commonSettings,
    mainClass in(Compile, run) := Some("es.david.Main")
  )
testOptions in Test ++=
  Seq(Tests.Argument(TestFrameworks.ScalaTest, "-osD"),
    Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
  )

lazy val `Day2` = (project in file("Day2"))
  .settings(
    commonSettings,
    mainClass in(Compile, run) := Some("es.david.Main"),
    libraryDependencies ++= Seq(
      scalaTest,
      "org.pegdown" % "pegdown" % "1.6.0" % "test" // No genera HTML
    )
  ).dependsOn(`Day1`)

addCommandAlias("testc", ";clean;coverage;test;coverageReport")
// Revisar para excluir en multimodulo
coverageExcludedPackages := "es\\.david\\.Main.*"

addCommandAlias("testa", "coverageAggregate")
coverageMinimum := 50
coverageFailOnMinimum := true

import scala.sys.process._

lazy val execScript = taskKey[Unit]("Execute the shell script")
execScript := {
  "clean_target.sh" !
}

// https://code.i-harness.com/en/q/17d6a55
lazy val deleteClass = taskKey[Unit]("Execute frontend scripts")

deleteClass := {
  val s: TaskStreams = streams.value
  val shell: Seq[String] = if (sys.props("os.name").contains("Windows")) Seq("cmd", "/c") else Seq("bash", "-c")
  val removeFiles: Seq[String] = shell :+ "/bin/bash ./clean_target.sh"
  //val ls: Seq[String] = shell :+ "ls ."
  s.log.info("Borrando class...")
  if ((removeFiles !) == 0) {
    s.log.success("Borrado ficheros successful!")
  } else {
    s.log.error("Error!")
    throw new IllegalStateException("frontend build failed!")
  }
  removeFiles !
}