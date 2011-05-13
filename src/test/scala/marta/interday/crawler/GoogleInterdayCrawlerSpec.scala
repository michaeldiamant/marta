package marta.interday.crawler

import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.net.URL
import java.io.File
import scala.io.Source

import org.junit.Assert.assertThat
import org.hamcrest.Matchers.is

@RunWith(classOf[JUnitRunner])
class GoogleInterdayCrawlerSpec extends FlatSpec {
    private val symbol = "ZVZZT"
    private val endDate = new DateTime()
    private val startDate = new DateTime(endDate.minusMonths(1))
    private val googleDateFormatPattern = "dd-MMM-yy"
    
    behavior of "A GoogleInterdayCrawler"
    
    it should "define " + googleDateFormatPattern + " date format pattern" in {
        val actualDateFormatPattern = new GoogleInterdayCrawler(symbol).getDateFormatPattern
        
        assertThat(actualDateFormatPattern, is(googleDateFormatPattern))
    }
    
    it should "build URL using the symbol, start date, and end date" in {
        val actualUrl = new GoogleInterdayCrawler(symbol, startDate, endDate).buildUrl
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
        val expectedUrl = new URL("http://finance.google.com/finance/historical?q=" + symbol + "&startdate=" +
                formatter.print(startDate) + "&enddate=" + 
                formatter.print(endDate) + "&output=csv")  
        
        assertThat(actualUrl, is(expectedUrl))
    }
}