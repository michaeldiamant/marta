package marta.interday.crawler

import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import org.joda.time.DateTime
import java.net.URL
import java.io.File
import scala.io.Source

import org.junit.Assert.assertThat
import org.hamcrest.Matchers.is

@RunWith(classOf[JUnitRunner])
class YahooInterdayCrawlerSpec extends FlatSpec {
    private val symbol = "ZVZZT"
    private val endDate = new DateTime()
    private val startDate = new DateTime(endDate.minusMonths(1))
    private val yahooDateFormatPattern = "yyyy-MM-dd"
    
    behavior of "A YahooInterdayCrawler"
    
    it should "define " + yahooDateFormatPattern + " date format pattern" in {
        val returnedDateFormatPattern = new YahooInterdayCrawler(symbol).getDateFormatPattern
        
        assertThat(returnedDateFormatPattern, is(yahooDateFormatPattern))
    }
    
    it should "build URL using the symbol, start date, and end date" in {
        val actualUrl = new YahooInterdayCrawler(symbol, startDate, endDate).buildUrl
        val expectedUrl = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + symbol + 
                "&a=" + startDate.minusMonths(1).getMonthOfYear + "&b=" + startDate.getDayOfMonth + "&c=" + startDate.getYear + 
                "&d=" + endDate.minusMonths(1).getMonthOfYear + "&e=" + endDate.getDayOfMonth + "&f=" + endDate.getYear + 
                "&g=d") 
        
        assertThat(actualUrl, is(expectedUrl))
    }
    
    it should "include OHLC line transform" in {
        val sampleLine = "2011-05-12,11.55,11.80,11.49,11.70,8000000,7.90"
        val crawler = new YahooInterdayCrawler(symbol, startDate, endDate)
        
        assertThat(crawler.lineTransforms.size, is(1))
        
        val expectedPrices = YahooInterdayCrawler.adjustOhlcPrices(sampleLine)
        val actualPrices = crawler.lineTransforms.head(sampleLine)
        
        assertThat(actualPrices, is(expectedPrices))
    }
}