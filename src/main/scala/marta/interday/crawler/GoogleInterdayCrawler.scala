package marta.interday.crawler

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.net.URL

class GoogleInterdayCrawler(
        symbol: String,
        startDate: DateTime = new DateTime(0),
        endDate: DateTime = new DateTime()
    ) extends InterdayCrawler(symbol, startDate, endDate) {
    
    private val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    
    def buildUrl(): URL = 
        new URL("http://finance.google.com/finance/historical?q=" + symbol + "&startdate=" +
                    formatter.print(startDate) + "&enddate=" + 
                    formatter.print(endDate) + "&output=csv")
    
    def getDateFormatPattern(): String =
        "dd-MMM-yy"
}
