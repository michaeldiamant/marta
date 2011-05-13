package marta.interday.crawler

import org.joda.time.DateTime
import scala.math.BigDecimal
import scala.math.BigDecimal.RoundingMode
import java.math.MathContext
import java.net.URL

/**
 * Yahoo! Finance implementation of the InterdayCrawler.
 */
class YahooInterdayCrawler (
        symbol: String,
        startDate: DateTime = new DateTime(0),
        endDate: DateTime = new DateTime()
    ) extends InterdayCrawler(symbol, startDate, endDate, List(YahooInterdayCrawler.adjustOhlcPrices)) {

    def buildUrl(): URL = 
        // Yahoo! Finance months are zero-indexed.
        new URL("http://ichart.finance.yahoo.com/table.csv?s=" + symbol + 
                "&a=" + startDate.minusMonths(1).getMonthOfYear + "&b=" + startDate.getDayOfMonth + "&c=" + startDate.getYear + 
                "&d=" + endDate.minusMonths(1).getMonthOfYear + "&e=" + endDate.getDayOfMonth + "&f=" + endDate.getYear + 
                "&g=d")
    
    def getDateFormatPattern(): String =
        "yyyy-MM-dd"
}

object YahooInterdayCrawler {
    
    /**
     * Uses adjusted close price provided by Yahoo! Finance to adjust OHLC for dividends and splits.
     * 
     * @param line CSV line containing date, open, high, low, close, volume, and adjusted close
     * @return CSV line containing date, adjusted open, high, low, close, and volume
     */
    def adjustOhlcPrices(line:String):String = {
        val delimiter = ","
        val openIndex = 1
        val closeIndex = 4
        val adjustedCloseIndex = 6
    
        def createBigDecimal(s: String) = BigDecimal(s, MathContext.DECIMAL128)
        
        val lineArray = line.split(delimiter)
        val ohlc = for (index <- openIndex until closeIndex + 1) yield createBigDecimal(lineArray(index))
        val adjustedClose = BigDecimal(lineArray(adjustedCloseIndex))
        val adjustFactor = ohlc.last / adjustedClose
        
        for (index <- openIndex until closeIndex + 1) {
            lineArray.update(index, (ohlc(index - 1) / adjustFactor).setScale(2, RoundingMode.UP).toString)    
        }
            
        def stripAdjustedClose(arr: Array[String]) = arr.elements.take(arr.size - 1)
        
        stripAdjustedClose(lineArray) mkString delimiter
    }
}

