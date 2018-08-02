package com.leodroidcoder.stockqoutes.presentation.chart

import com.arellomobile.mvp.InjectViewState
import com.github.mikephil.charting.data.Entry
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import com.leodroidcoder.stockqoutes.presentation.chart.chart.ChartModel
import io.reactivex.Flowable
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Named

/**
 * Chart screen Presenter.
 * Loads data from repository. Controls [ChartMvpView] and passes data to/from it
 * Manages navigation to other screens.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@InjectViewState
class ChartPresenter @Inject constructor(
        router: Router?,
        private val repository: QuotesRepository,
        @Named(ChartModule.SYMBOL_QUALIFIER) private val symbol: String
) : BasePresenter<ChartMvpView>(router) {

    override fun attachView(view: ChartMvpView) {
        super.attachView(view)
        getData()
    }

    override fun setTitle() {
        viewState?.setupToolbar(symbol, true)
    }

    /**
     * Load ticks data for a specific currency symbol [symbol] from repository.
     * Provides always fresh data [Flowable], once it is changed in data source.
     *
     * @since 1.0.0
     */
    private fun getData() {
        repository.getTicks(symbol)
                .filter { it.isNotEmpty() }
                .map { it -> mapToChartModel(it) }
                .subscribeOn(AppSchedulers.computation())
                .observeOn(AppSchedulers.mainThread())
                .subscribe(
                        {
                            viewState?.showData(it)
                        },
                        ::defaultOnError)
                .addTo(compositeDisposable)
    }

    /**
     * Maps ticks into a chart-friendly [ChartModel] format.
     * Uses relative time, starting from the first tick time (for chart appearance)
     *
     * @param input not empty list of Ticks.
     * @return chart-friendly [ChartModel]
     *
     * @since 1.0.0
     */
    private fun mapToChartModel(input: List<Tick>): ChartModel {
        val firstTime = input[0].date.time
        val asks = mutableListOf<Entry>()
        val bids = mutableListOf<Entry>()
        input.forEach {
            val relativeTime = it.date.time.minus(firstTime)
            asks.add(Entry(relativeTime.toFloat(), it.ask.toFloat()))
            bids.add(Entry(relativeTime.toFloat(), it.bid.toFloat()))
        }
        return ChartModel(firstTime, asks, bids)
    }
}