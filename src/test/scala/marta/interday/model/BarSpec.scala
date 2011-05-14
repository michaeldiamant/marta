package marta.interday.model

import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import org.joda.time.DateTime
import java.net.URL
import java.io.File
import scala.math.BigDecimal
import scala.io.Source

import org.junit.Assert.assertThat
import org.hamcrest.Matchers.is

@RunWith(classOf[JUnitRunner])
class BarSpec extends FlatSpec {
    private val symbol = "ZVZZT"
    private val date = new DateTime()
    private val open = BigDecimal(1)
    private val high = BigDecimal(10)
    private val low = BigDecimal(0.5)
    private val close = BigDecimal(5)
    private val volume = 1000L
    
    behavior of "A Bar"
    
    it should "require a non-empty symbol" in {
        intercept[IllegalArgumentException] {
            new Bar("", date, open, high, low, close, volume)
        }
    }

    it should "require a non-null date" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, null, open, high, low, close, volume)
        }
    }
    
    it should "require low price to be at least as low as open price" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, date, low - 1, high, low, close, volume)
        }        
    }
    
    it should "require low price to be at least as low as high price" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, date, open, low - 1, low, close, volume)
        }        
    }
    
    it should "require low price to be at least as low as close price" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, date, open, high, low, low - 1, volume)
        }        
    }    
    
    it should "require high price to be least as high as open price" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, date, high + 1, high, low, close, volume)
        }        
    }
    
    it should "require a positive open price" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, date, -1, high, low, close, volume)
        }
    }
    
    it should "require a positive close price" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, date, open, high, low, -1, volume)
        }        
    }
    
    it should "require a positive volume" in {
        intercept[IllegalArgumentException] {
            new Bar(symbol, date, open, high, low, close, -1)
        }
    } 
    
    it should "produce a Bar given a CSV line" in {
        val open = BigDecimal("12.35")
        val high = BigDecimal("12.45")
        val low = BigDecimal("12.30")
        val close = BigDecimal("12.36")
        val volume = 11613000L
        
        val bar = Bar(symbol + ",2010-11-23," + (List(open.toString, high.toString, low.toString, 
                close.toString, volume.toString) mkString ","))
        
        assertThat(bar.open, is(open))
        assertThat(bar.high, is(high))
        assertThat(bar.low, is(low))
        assertThat(bar.close, is(close))
        assertThat(bar.volume, is(volume))
    }
}