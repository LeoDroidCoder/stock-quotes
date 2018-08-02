package com.leodroidcoder.stockqoutes.presentation.symbols

import com.arellomobile.mvp.InjectViewState
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import com.leodroidcoder.stockqoutes.subscribeBy
import io.reactivex.Flowable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Symbols screen Presenter.
 * Loads data from repository. Controls [SymbolsMvpView] and passes data to/from it
 * Manages navigation to other screens.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@InjectViewState
class SymbolsPresenter @Inject constructor(
        router: Router?,
        private val repository: QuotesRepository
) : BasePresenter<SymbolsMvpView>(router) {

    override fun attachView(view: SymbolsMvpView) {
        super.attachView(view)
        getSymbols()
    }

    override fun setTitle() {
        viewState.setupToolbar(backEnabled = true)
    }

    /**
     * Load currency symbols from repository.
     * Provides always fresh data [Flowable], once it is changed in data source.
     *
     * @since 1.0.0
     */
    private fun getSymbols() {
        repository.getSymbolSubscriptions()
                .subscribeOn(AppSchedulers.computation())
                .observeOn(AppSchedulers.mainThread())
                .subscribe(viewState::onSymbols, ::defaultOnError)
                .addTo(compositeDisposable)
    }

    /**
     * Called when user checked/unchecked a subscription.
     * Sends data to repository correspondingly.
     *
     * @since 1.0.0
     */
    fun symbolSubscriptionCheck(symbol: String, checked: Boolean) {
        (if (checked) repository.subscribeForSymbol(symbol) else repository.unsubscribeFromSymbol(symbol))
                .subscribeBy(::defaultOnError)
                .addTo(compositeDisposable)
    }
}