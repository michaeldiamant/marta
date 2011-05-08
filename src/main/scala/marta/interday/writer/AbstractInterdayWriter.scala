package marta.interday.writer

import marta.interday.model.Bar

abstract class AbstractInterdayWriter {

    def write(bars: List[Bar])
}