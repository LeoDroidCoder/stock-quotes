package com.leodroidcoder.stockqoutes.presentation.symbols

import com.arellomobile.mvp.InjectViewState
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepositoryImpl
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by leonid on 9/26/17.
 */
@InjectViewState
class SymbolsPresenter @Inject constructor(
        private val router: Router?,
        private val repository: QuotesRepositoryImpl
) : BasePresenter<SymbolsMvpView>(router) {


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getSymbols()
    }

    private fun getSymbols() {
        repository.getSymbolSubscriptions()
                .subscribeOn(AppSchedulers.computation())
                .observeOn(AppSchedulers.mainThread())
                .subscribe(viewState::onSymbols, ::defaultOnError)
                .addTo(compositeDisposable)
    }

    fun symbolSubscriptionToggle(symbol: String, checked: Boolean) {
        repository.symbolsSubscriptionToggle(listOf(symbol), checked)
                .subscribe({ Timber.d("Symbol subscription. symbol=$symbol, subscribe=$checked") }, ::defaultOnError)
                .addTo(compositeDisposable)
    }
}