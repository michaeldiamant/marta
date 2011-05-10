package marta.interday.model

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.math.BigDecimal

/**
 * Representation of open, high, low, close, volume (OHLCV) data for one trading day.
 */
class Bar(
        private val symbol: String,
        private val date: DateTime,
        private val open: BigDecimal,
        private val high: BigDecimal,
        private val low: BigDecimal,
        private val close: BigDecimal,
        private val volume: Long
    ) {
    
    require(!symbol.isEmpty)
    
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