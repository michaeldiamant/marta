package marta.interday.main

import marta.interday.crawler.{GoogleInterdayCrawler, YahooInterdayCrawler}
import marta.interday.writer._

object Main extends App {
  val bars = new YahooInterdayCrawler("AAPL").crawl
  val writer = new InterdayFileWriter("/tmp/results")
  writer.write(bars)

}