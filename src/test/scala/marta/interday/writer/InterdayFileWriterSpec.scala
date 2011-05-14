package marta.interday.writer

import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import java.io.File
import scala.io.Source
import marta.interday.model.Bar
import org.apache.commons.io.FileUtils

import org.junit.Assert.assertThat
import org.hamcrest.Matchers.is

@RunWith(classOf[JUnitRunner])
class InterdayFileWriterSpec extends FlatSpec {

    behavior of "An InterdayFileWriter"
    
    it should "require a non-empty filename" in {
        intercept[IllegalArgumentException] {
            new InterdayFileWriter("")
        }
    }
    
    it should "produce a file with one row for each Bar" in {
        def loadLines(file: File) = Source.fromFile(file).getLines.drop(1).toList
        def prependSymbol(lines: List[String]) = lines.map("ZVZZT," + _)
        def createBars(lines: List[String]) = lines.map(Bar(_))
        
        val originalFile = new File("src/test/resources/sample_data.csv")
        val fileToWrite = new File(FileUtils.getTempDirectoryPath + "/test_file.csv")
        val originalBars = createBars(prependSymbol(loadLines(originalFile)))
            
        val writer = new InterdayFileWriter(fileToWrite.getAbsolutePath)
        writer.write(originalBars)

        val writtenBars = createBars(loadLines(fileToWrite))
        FileUtils.deleteQuietly(fileToWrite)
        
        assertThat(writtenBars.size, is(originalBars.size))
        
        originalBars.zip(writtenBars).foreach(barPair => {
                val firstBar = barPair._1
                val secondBar = barPair._2
                assertThat(firstBar.symbol, is(secondBar.symbol))
                assertThat(firstBar.date, is(secondBar.date))
                assertThat(firstBar.open, is(secondBar.open))
                assertThat(firstBar.high, is(secondBar.high))
                assertThat(firstBar.low, is(secondBar.low))
                assertThat(firstBar.close, is(secondBar.close))
                assertThat(firstBar.volume, is(secondBar.volume))                
            }
        )
    }
}