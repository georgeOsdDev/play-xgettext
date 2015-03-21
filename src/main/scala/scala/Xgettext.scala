package scala

import java.io.{BufferedWriter, File, FileWriter}
import scala.collection.mutable.{HashMap => MHashMap, MultiMap, Set => MSet}
import scala.tools.nsc
import nsc.Global
import nsc.Phase
import nsc.plugins.Plugin
import nsc.plugins.PluginComponent

// http://www.scala-lang.org/node/140
class Xgettext(val global: Global) extends Plugin {
  import global._

  type i18nValue = (
    String,          // source
    Int              // line
  )

  type i18nValues = MSet[i18nValue]

  val name        = "play-xgettext"
  val description = "This Scala compiler plugin extracts and creates messages.default file for PlayFramework"
  val components  = List[PluginComponent](MapComponent, ReduceComponent)

  val OUTPUT_FILE     = "messages.default"

  val outputFile            = new File(OUTPUT_FILE)
  val emptyOutputFileExists = outputFile.exists && outputFile.isFile && outputFile.length == 0

  val msgToLines = new MHashMap[String, i18nValues] with MultiMap[String, i18nValue]

  // Avoid running ReduceComponent multiple times
  var reduced = false

  var hideLines = false

  override def processOptions(options: List[String], error: String => Unit) {
    for (option <- options) {
      hideLines = option == "hideLines"
    }
  }

  private object MapComponent extends PluginComponent {
    val global: Xgettext.this.global.type = Xgettext.this.global

    val runsAfter = List("refchecks")

    val phaseName = "xgettext-map"

    def newPhase(_prev: Phase) = new MapPhase(_prev)

    class MapPhase(prev: Phase) extends StdPhase(prev) {
      override def name = phaseName

      val doubleQuote = """""""

      def apply(unit: CompilationUnit) {

        for (tree @ Apply(Select(x1, x2), list) <- unit.body) {

          if (x1.tpe.toString == "play.api.i18n.Messages.type") {
            val msgKey = list(0).toString
            if (msgKey.startsWith(doubleQuote) & msgKey.endsWith(doubleQuote)) {
              val pos    = tree.pos  // scala.tools.nsc.util.OffsetPosition
              val line   = (relPath(pos.source.path), pos.line)
              val msgid  = msgKey.drop(1).dropRight(1) // remove "
              msgToLines.addBinding(msgid, line)
            }
          }
        }

      }

      private def relPath(absPath: String) = {
        val curDir   = System.getProperty("user.dir")
        val relPath  = absPath.substring(curDir.length)
        val unixPath = relPath.replace("\\", "/")  // Windows uses '\' to separate
        unixPath
      }

    }
  }

  private object ReduceComponent extends PluginComponent {
    val global: Xgettext.this.global.type = Xgettext.this.global

    val runsAfter = List("jvm")

    val phaseName = "xgettext-reduce"

    def newPhase(_prev: Phase) = new ReducePhase(_prev)

    class ReducePhase(prev: Phase) extends StdPhase(prev) {
      override def name = phaseName

      def apply(unit: CompilationUnit) {
        if (emptyOutputFileExists && !reduced) {
          val builder = new StringBuilder()

          // Sort by key (msgctxto, msgid, msgidPluralo)
          // so that it's easier too see diffs between versions of the .pot/.po file
          val sortedMsgToLines = msgToLines.toSeq.sortBy { case (k, v) => k }

          for ((msgid , lines) <- sortedMsgToLines) {
            val sortedLines = lines.toSeq.sorted
            if (!hideLines) {
              for ((srcPath, lineNo) <- sortedLines) {
                builder.append("#: " + srcPath + ":" + lineNo + "\n")
              }
              builder.append(s"${msgid}=\n\n")
            } else {
              builder.append(s"${msgid}=\n")
            }
          }

          val out = new BufferedWriter(new FileWriter(outputFile))
          out.write(builder.toString)
          out.close()
          println(OUTPUT_FILE + " created")

          reduced = true
        }
      }
    }
  }
}