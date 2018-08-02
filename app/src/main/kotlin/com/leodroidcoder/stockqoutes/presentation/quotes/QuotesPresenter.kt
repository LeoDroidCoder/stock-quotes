package com.leodroidcoder.stockqoutes.presentation.quotes

import com.arellomobile.mvp.InjectViewState
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.domain.entity.TickOrder
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import com.leodroidcoder.stockqoutes.presentation.navigation.Screens
import io.reactivex.Flowable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Quotes screen Presenter.
 * Loads data from repository. Controls [QuotesMvpView] and passes data to/from it
 * Manages navigation to other screens.
 *
 * Lets navigating to screens [Screens.SCREEN_CHART], [Screens.SCREEN_SYMBOLS]
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@InjectViewState
class QuotesPresenter @Inject constructor(
        private val router: Router?,
        private val repository: QuotesRepository
) : BasePresenter<QuotesMvpView>(router) {

    private var ticks = listOf<Tick>()
    private var tickOrder = TickOrder.SYMBOL_ASC
        set(value) {
            field = value
            applyOrder()
            showData()
        }

    override fun attachView(view: QuotesMvpView) {
        super.attachView(view)
        subscribeForLastSymbolTicks()
    }

    override fun setTitle() {
        viewState?.setupToolbar()
    }

    /**
     * Load currency symbols ticks, for which user is subscribed
     * Provides always fresh data [Flowable], once it is changed in data source.
     * Sorts data accordingly to selected [TickOrder].
     * Passes data to the View once it is ready.
     *
     * @since 1.0.0
     */
    private fun subscribeForLastSymbolTicks() {
        repository.getLastSymbolTicks()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.mainThread())
                .subscribe(
                        {
                            saveData(it)
                            applyOrder()
                            showData()
                        }, ::defaultOnError
                )
                .addTo(compositeDisposable)
    }

    private fun saveData(ticks: List<Tick>) {
        this.ticks = ticks
    }

    /**
     * Sort ticks according to the order [tickOrder]
     *
     * @see TickOrder
     *
     * @since 1.0.0
     */
    private fun applyOrder() {
        ticks = ticks.sortedWith(tickOrder.compareTick()).toList()
    }

    /**
     * Pass ticks data to the view
     *
     * @since 1.0.0
     */
    private fun showData() {
        viewState?.showData(ticks)
    }

    /**
     * Navigate to symbols screen
     *
     * @since 1.0.0
     */
    fun onSymbolsClick() {
        router?.navigateTo(Screens.SCREEN_SYMBOLS)
    }

    /**
     * Navigate to the chart screen
     *
     * @param symbol clicked item's symbol
     *
     * @since 1.0.0
     */
    fun onItemClick(symbol: String) {
        router?.navigateTo(Screens.SCREEN_CHART, symbol)
    }

    /**
     * Called when user explicitly changes sorting order by a symbol [Tick.symbol]
     * When is not checked [checked], [TickOrder.SYMBOL_ASC] order should be applied,
     * and when it IS checked - [TickOrder.SYMBOL_DESC] should be applied instead.
     * Is not checked by default
     *
     * @since 1.0.0
     */
    fun onSymbolsOrderCheck(checked: Boolean) {
        tickOrder = if (checked) TickOrder.SYMBOL_DESC else TickOrder.SYMBOL_ASC
    }
}

