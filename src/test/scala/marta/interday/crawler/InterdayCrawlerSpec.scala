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
class InterdayCrawlerSpec extends FlatSpec {
    private val symbol = "ZVZZT"
    private val endDate = new DateTime()
    private val startDate = new DateTime(endDate.minusHours(1))
    private val dateFormatPattern = "yyyy-MM-dd"
    private val url = "test_url"
    
    behavior of "An InterdayCrawler"
    
    it should "require a non-empty symbol" in {
        intercept[IllegalArgumentException] {
            new InterdayCrawler("", startDate, endDate) {
                def getDateFormatPattern() = dateFormatPattern
                def buildUrl() = new URL(url)
            }
        }
    }
    
    it should "require the start date to be before the end date" in {
        val before = new DateTime()
        val after = new DateTime()
        
        intercept[IllegalArgumentException] {
            new InterdayCrawler(symbol, after, before) {
                def getDateFormatPattern() = dateFormatPattern
                def buildUrl() = new URL(url)
            }
        }      
    }
    
    it should "require a non-empty date format pattern" in {
        intercept[IllegalArgumentException] {
            new InterdayCrawler(symbol, startDate, endDate) {
                def getDateFormatPattern() = ""
                def buildUrl() = new URL(url)
            }
        }        
    }
    
    it should "drop the first line of data if it is a header" in {
        val dataFile = new File("src/test/resources/sample_data.csv")
        val crawler = new InterdayCrawler(symbol, startDate, endDate) {
                def getDateFormatPattern() = "yyyy-MM-dd"
                def buildUrl() = dataFile.toURI.toURL
            }
        
        val bars = crawler.crawl        
        
        val expectedLineCount = Source.fromFile(dataFile).getLines.drop(1).size
        
        assertThat(bars.size, is(expectedLineCount))   
    }
    
    it should "keep the first line of data if it is not a header" in {
        val dataFile = new File("src/test/resources/sample_data_with_no_header.csv")
        val crawler = new InterdayCrawler(symbol, startDate, endDate) {
                def getDateFormatPattern() = "yyyy-MM-dd"
                def buildUrl() = dataFile.toURI.toURL
                override def providesHeader() = false
            }
        
        val bars = crawler.crawl        
        
        val expectedLineCount = Source.fromFile(dataFile).getLines.size
        
        assertThat(bars.size, is(expectedLineCount))
    }
    
    it should "populate the symbol of each Bar" in {
        val crawler = new InterdayCrawler(symbol, startDate, endDate) {
                def getDateFormatPattern() = "yyyy-MM-dd"
                def buildUrl() = new File("src/test/resources/sample_data.csv").toURI.toURL
            }
        
        val bars = crawler.crawl        
        
        bars.foreach(bar => assertThat(bar.symbol, is(symbol)))
    }
    
    it should "produce date ascending sorted list of Bars" in {
        val crawler = new InterdayCrawler(symbol, startDate, endDate) {
                def getDateFormatPattern() = "yyyy-MM-dd"
                def buildUrl() = new File("src/test/resources/unsorted_data.csv").toURI.toURL
            }
        
        val bars = crawler.crawl        
        
        var previousDate = new DateTime(0)
        bars.foreach(bar => {
                assertThat(previousDate.isBefore(bar.date), is(true))
                previousDate = bar.date
            }
        )
    }
}