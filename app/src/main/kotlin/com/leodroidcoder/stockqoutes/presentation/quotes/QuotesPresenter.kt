package com.leodroidcoder.stockqoutes.presentation.quotes

import com.arellomobile.mvp.InjectViewState
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.domain.entity.TickOrder
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import com.leodroidcoder.stockqoutes.presentation.common.navigation.Screens
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by leonid on 9/26/17.
 */
@InjectViewState
class QuotesPresenter @Inject constructor(
    private val router: Router?,
    private val repository: QuotesRepository
) : BasePresenter<QuotesMvpView>(router) {

    var tickOrder = TickOrder.SYMBOL_ASC
        set(value) {
            field = value
            applyOrder()
            showData()
        }

    private var ticks = listOf<Tick>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        subscribeForLastSymbolTicks()
    }

    private fun subscribeForLastSymbolTicks() {
        repository.getLastSymbolTicks()
            .subscribeOn(AppSchedulers.computation())
            .observeOn(AppSchedulers.mainThread())
            .subscribe(
                {
                    //todo temp
                    Timber.d("quotes: $it")
                    viewState.showData(it)
//                    saveData(it)
//                    applyOrder()
//                    showData()
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
     */
    private fun applyOrder() {
        ticks = ticks.sortedWith(tickOrder.compareTick()).toList()
    }

    /**
     * Pass ticks data to the view
     */
    private fun showData() {
        viewState.showData(ticks)
    }

    /**
     * Navigate to symbols screen
     */
    fun onSymbolsClick() {
        router?.navigateTo(Screens.SCREEN_SYMBOLS)
    }

    /**
     * Navigate to the chart screen
     *
     * @param symbol clicked item's symbol
     */
    fun onItemClick(symbol: String) {
        router?.navigateTo(Screens.SCREEN_CHART, symbol)
    }
}

