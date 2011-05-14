package marta.interday.model

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.math.BigDecimal

/**
 * Representation of open, high, low, close, volume (OHLCV) data for one trading day.
 */
class Bar(
        val symbol: String,
        val date: DateTime,
        val open: BigDecimal,
        val high: BigDecimal,
        val low: BigDecimal,
        val close: BigDecimal,
        val volume: Long
    ) {
    
    require(!symbol.isEmpty)
    require(null != date)
    require(volume >= 0)
    require(open >= 0)
    require(close >= 0)
    require(low <= open && low <= high && low <= close)
    require(high >= open && high >= low && high >= close)
    
    private val delimiter = ","
    
    override def toString(): String =
        new StringBuilder() :+ symbol :+ DateTimeFormat.forPattern("yyyy-MM-dd").print(date) :+ 
        open :+ high :+ low :+ close :+ volume mkString Bar.DELIMITER
}
    
object Bar {
    private val DELIMITER = ","
    val csvHeader = new StringBuilder() :+ "symbol" :+ "date" :+ "open" :+ "high" :+ "low" :+ "close" :+ "volume" mkString DELIMITER

    /**
     * Creates a Bar from a comma separated string.  Ordering of line contents must be symbol,
     * date, open, high, low, close, volume.
     */
    def apply(line: String): Bar = {
        val lineArray = line.split(DELIMITER)
        
        new Bar(lineArray(0), 
                new DateTime(lineArray(1)),
                BigDecimal(lineArray(2)), 
                BigDecimal(lineArray(3)), 
                BigDecimal(lineArray(4)), 
                BigDecimal(lineArray(5)), 
                lineArray(6).toLong)
    }
}