package marta.interday.writer

import scala.collection.JavaConversions._
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.ArrayList
import marta.interday.model.Bar

class InterdayFileWriter(
        val filename: String
    ) extends AbstractInterdayWriter {

    def write(bars: List[Bar]) = {
        val linesWithHeader = Bar.csvHeader +: bars.map(_.toString)
        FileUtils.writeLines(new File(filename), new ArrayList[String](linesWithHeader.toBuffer))
    }
}