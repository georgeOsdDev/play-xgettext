organization := "tv.cntt"

name := "play-xgettext"

version := "0.1-SNAPSHOT"

// When doing publish-signed, change the version below and those line accordingly.
scalaVersion := "2.11.5"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies <+= scalaVersion { sv =>
  "org.scala-lang" % "scala-compiler" % sv
}
