package marta.interday.main

import marta.interday.crawler.{GoogleInterdayCrawler, YahooInterdayCrawler}
import marta.interday.writer._

object Main {

    def main(args: Array[String]): Unit = {
        val bars = new YahooInterdayCrawler("AAPL").crawl
        val writer = new InterdayFileWriter("""C:\AAPL.csv""")
        writer.write(bars)
    }
}