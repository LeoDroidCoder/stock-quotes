package com.leodroidcoder.stockqoutes.presentation.chart

import com.arellomobile.mvp.InjectViewState
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepositoryImpl
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by leonid on 9/26/17.
 */
@InjectViewState
class ChartPresenter @Inject constructor(
        private val router: Router?,
        private val repository: QuotesRepositoryImpl,
        @Named(ChartModule.SYMBOL_QUALIFIER) private val symbol: String
) : BasePresenter<ChartMvpView>(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getData()
    }


    // todo map to chart-friendly and pass to view
    private fun getData() {
        repository.getTicks(symbol)
                .subscribeOn(AppSchedulers.computation())
                .observeOn(AppSchedulers.mainThread())
                .subscribe(
                        {
                            Timber.d("Ticks: %s", it)
                        },
                        ::defaultOnError)
                .addTo(compositeDisposable)
    }
}