package marta.interday.writer

import marta.interday.model.Bar

/**
 * Abstract class for writing Bars to an implementation-specific data source.
 */
abstract class AbstractInterdayWriter {

    /**
     * Writes the provided Bars to the data source.
     * 
     * @param bars the Bars to write
     */
    def write(bars: List[Bar])
}