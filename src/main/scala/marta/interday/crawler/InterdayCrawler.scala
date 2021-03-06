package marta.interday.crawler

import marta.interday.model.Bar
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.net.URL
import scala.io.Source
import scala.collection.mutable.StringBuilder

/**
 * Parses interday OHLCV market data from publicly available market data websites.
 */
abstract class InterdayCrawler(
        val symbol: String,
        val startDate: DateTime,
        val endDate: DateTime,
        val lineTransforms: List[String => String] = List()
    ) {
    
    require(!symbol.isEmpty) 
    require(startDate.isBefore(endDate))
    require(!getDateFormatPattern.isEmpty)
   
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
    
    /**
     * Crawls a market data provider's interday data.
     * 
     * @return list of bars representing the crawled OHLCV data
     */
    final def crawl(): List[Bar] = {
        val linesToDrop = providesHeader match { case true => 1; case _ => 0 }
        val originalLines = Source.fromURL(buildUrl).getLines().drop(linesToDrop).toList
        
        val processedLines:List[String] = originalLines.map(line => { 
            var processedLine:String = line
            crawlerLineTransforms.foreach(f => processedLine = f(processedLine))
           
            processedLine
        })
        
        val sortedLines = processedLines.sortWith((line1, line2) => {
            def toDateTime(line: String) = new DateTime(line.split(delimiter)(dateIndex))
            toDateTime(line1).isBefore(toDateTime(line2))
        })
        
        sortedLines.map(Bar(_))
    }
    
    /**
     * Defines if the market data provider prefixes its data with a header.  By default, it is assumed
     * that a header is present.
     * 
     * @return true if the market data provider prepends a header to its data, false otherwise
     */
    def providesHeader(): Boolean = true
    
    /**
     * Gets the date format pattern that matches the format of crawled dates.  See 
     * {@link http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html} 
     * for expected pattern syntax.
     * 
     * @return date format pattern
     */
    def getDateFormatPattern(): String
    
    /**
     * Builds URL used to crawl market data.  The URL should include the symbol and the start and end
     * dates as expected by the market data provider.
     * 
     * @return url to retrieve market data from
     */
    def buildUrl(): URL
}