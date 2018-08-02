package com.leodroidcoder.stockqoutes.presentation.chart


import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.color
import com.leodroidcoder.stockqoutes.presentation.base.BaseFragment
import com.leodroidcoder.stockqoutes.presentation.chart.chart.ChartModel
import com.leodroidcoder.stockqoutes.presentation.chart.chart.TimeAxisValueFormatter
import com.leodroidcoder.stockqoutes.presentation.quotes.QuotesPresenter
import kotlinx.android.synthetic.main.fragment_chart.*

/**
 * Chart screen Fragment.
 * Shows a currency pair Bid/Ask chart, which is updated in real time.
 *
 * Receives data from the presenter [QuotesPresenter] and shows it.
 * Passes user interaction events to presenter.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class ChartFragment : BaseFragment<ChartMvpView, ChartPresenter>(), ChartMvpView {

    @InjectPresenter
    lateinit var presenter: ChartPresenter

    @ProvidePresenter
    fun providePresenter(): ChartPresenter = lazyPresenter.get()

    companion object {


        const val ARGUMENT_SYMBOL = "ARGUMENT_SYMBOL"

        /**
         * Returns new instance of current fragment.
         * Supply it with the arguments if needed.
         *
         * @return new instance of current fragment.
         *
         * @since 1.0.0
         */
        fun newInstance(symbol: String) = ChartFragment()
                .apply {
                    val args = Bundle()
                    args.putString(ARGUMENT_SYMBOL, symbol)
                    arguments = args
                }

    }

    override fun getLayoutResId() = R.layout.fragment_chart

    override fun setupToolbar(title: String, backEnabled: Boolean) {
        setToolbar(title, backEnabled)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChart()
    }

    /**
     * Set up chart.
     *
     * @since 1.0.0
     */
    private fun setupChart() {
        chart?.run {
            // remove unneeded y and left x axis
            isScaleYEnabled = false
            axisLeft.isEnabled = false
            // eremove descriptions
            description = null
            setNoDataText(null)
            // remove bg and borders
            setDrawGridBackground(false)
            setDrawBorders(false)
            // adjust x range
            setVisibleXRangeMinimum(3f)
        }
    }

    /**
     * Show chart.
     * Performs view-specific chart data transformations
     * and populates the chart.
     *
     * @since 1.0.0
     */
    override fun showData(chartData: ChartModel) {
        // construct data set from the bid and ask data
        val dataSetAsk = LineDataSet(chartData.ask, getString(R.string.chart_legend_ask))
        val dataSetBid = LineDataSet(chartData.bid, getString(R.string.chart_legend_bid))
        setDataSet(dataSetAsk)
        setDataSet(dataSetBid)
        context?.let {
            dataSetAsk.color = it.color(R.color.color_chart_ask)
            dataSetBid.color = it.color(R.color.color_chart_bid)
        }

        // setup chart-data-set-specific parameters and populate the chart
        // needed to create "Date" X axis scale.
        val xAxisFormatter = TimeAxisValueFormatter(chartData.referenceTime)
        chart?.run {
            xAxis?.run {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = xAxisFormatter

                data = LineData(dataSetAsk, dataSetBid)
                invalidate()
            }
        }
    }

    private fun setDataSet(dataSet: LineDataSet) = dataSet.apply {
        mode = LineDataSet.Mode.LINEAR
        setDrawCircles(false)
        setDrawValues(false)
    }
}
