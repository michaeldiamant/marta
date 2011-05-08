package marta.interday.crawler

import marta.interday.model.Bar
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.net.URL
import scala.io.Source
import scala.collection.mutable.StringBuilder

abstract class InterdayCrawler(
        private val symbol: String,
        private val startDate: DateTime,
        private val endDate: DateTime,
        private val lineTransforms: List[String => String] = List()
    ) {
    
    require(!symbol.isEmpty) 
   
    protected val delimiter = ","
    private val dateIndex = 1
    private val prependSymbol = (line: String) => new StringBuilder() :+ symbol :+ line mkString delimiter
    private val formatDate = (line: String) => {
        getDateFormatPattern match {
            case "yyyy-MM-dd" => line
            case _ => {
                val lineArray = line.split(delimiter)
                val date = DateTimeFormat.forPattern(getDateFormatPattern).parseDateTime(lineArray(dateIndex))
                val formattedDateString = DateTimeFormat.forPattern("yyyy-MM-dd").print(date)
                lineArray.update(dateIndex, formattedDateString)
                
                lineArray.mkString(delimiter)                
            }
        }
    }
    
    private val crawlerLineTransforms = lineTransforms ::: List(prependSymbol, formatDate)
        
    final def crawl(): List[Bar] = {
        val originalLines = Source.fromURL(buildUrl).getLines().drop(1).toList
        
        val processedLines:List[String] = originalLines.map(line => { 
            var processedLine:String = line
            crawlerLineTransforms.foreach(f => processedLine = f(processedLine))
           
            processedLine
        })
        
        val sortedLines = processedLines.sort((line1, line2) => {
            def toDateTime(line: String) = new DateTime(line.split(delimiter)(dateIndex))
            toDateTime(line1).isBefore(toDateTime(line2))
        })
        
        sortedLines.map(Bar(_))
    }
    
    def getDateFormatPattern(): String
    
    def buildUrl(): URL
}