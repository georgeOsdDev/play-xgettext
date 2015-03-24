organization := "com.github.georgeosddev"

name := "play-xgettext"

version := "0.1"

// When doing publish-signed, change the version below and those line accordingly.
scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.11.6", "2.10.5")

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies <+= scalaVersion { sv =>
  "org.scala-lang" % "scala-compiler" % sv
}

useGpg := true

publishTo <<= (version) { version: String =>
  val nexus = "https://oss.sonatype.org/"
  if (version.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else                                   Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/georgeosddev/play-xgettext</url>
  <licenses>
  <license>
  <name>MIT</name>
  <url>https://github.com/georgeosddev/play-xgettext/blob/master/MIT-LICENSE</url>
  <distribution>repo</distribution>
  </license>
  </licenses>
  <scm>
  <url>git@github.com:georgeosddev/play-xgettext.git</url>
  <connection>scm:git:git@github.com:georgeosddev/play-xgettext.git</connection>
  </scm>
  <developers>
  <developer>
  <id>georgeosddev</id>
  <name>Takeharu Oshida</name>
  <url>https://github.com/georgeosddev</url>
  </developer>
  </developers>
)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
