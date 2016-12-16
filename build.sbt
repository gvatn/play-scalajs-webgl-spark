lazy val scalaV = "2.11.8"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.0.0",
    specs2 % Test,
    "org.apache.spark" %% "spark-core" % "2.0.2",
    "org.apache.spark" %% "spark-streaming" % "2.0.2",
    "org.twitter4j" % "twitter4j-core" % "[4.0,)",
    "org.twitter4j" % "twitter4j-stream" % "[4.0,)"
  ),
  dependencyOverrides ++= Set(
    "com.fasterxml.jackson.core" % "jackson-core" % "2.6.5",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.5"
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(PlayScala).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := false,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  ),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs, macros)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js


lazy val macros = (project in file("macros")).settings(
  scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaV,
    "commons-codec" % "commons-codec" % "1.10",
    "commons-io" % "commons-io" % "2.5"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases")
  )
)


// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
