package com.leodroidcoder.stockqoutes.presentation.chart.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Used to format timestamp to time in format"HH:mm:ss",
 * in order to show it on axis.
 * @property minimalTimestamp minimum timestamp in data set
 */
class TimeAxisValueFormatter(
        private val minimalTimestamp: Long
) : IAxisValueFormatter {
    private val dataFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    private val date = Date()

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     * Real time can be calculated as [minimalTimestamp] + [value]
     *
     * @param value the value to be formatted. It's a relative time (real time - [minimalTimestamp])
     * @param axis  the axis the value belongs to
     */
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        // calculate real time and format it
        val originalTimestamp = minimalTimestamp + value.toLong()
        return formatDate(originalTimestamp)
    }

    /**
     * Formats time with use of [dataFormat].
     *
     * @param timeMills time in milliseconds
     */
    private fun formatDate(timeMills: Long): String {
        date.time = timeMills
        return dataFormat.format(date)
    }
}