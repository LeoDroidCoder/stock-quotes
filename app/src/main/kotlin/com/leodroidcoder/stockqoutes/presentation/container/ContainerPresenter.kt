package com.leodroidcoder.stockqoutes.presentation.container

import com.arellomobile.mvp.InjectViewState
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import com.leodroidcoder.stockqoutes.presentation.navigation.Screens
import com.leodroidcoder.stockqoutes.subscribeBy
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Container screen Presenter.
 * Loads data from repository. Controls [ContainerMvpView] and passes data to/from it
 * Manages navigation to other screens.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@InjectViewState
class ContainerPresenter @Inject constructor(
        private val router: Router,
        private val repository: QuotesRepository
) : BasePresenter<ContainerMvpView>(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.navigateTo(Screens.SCREEN_QUOTES)
        connectToSockets()
        subscribeForConnectivityChanges()
    }

    /**
     * Establish connection to sockets and subscribe for topics.
     *
     * @since 1.0.0
     */
    private fun connectToSockets() {
        repository.keepSocketsConnection()
                .subscribeOn(AppSchedulers.io())
                .subscribeBy(::defaultOnError)
    }

    /**
     * Subscribe for connectivity to sockets changes
     * in order to get notified when we are connected/disconnected.
     *
     * @since 1.0.0
     */
    private fun subscribeForConnectivityChanges() {
        repository.subscribeForConnectivityChanges()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.mainThread())
                .subscribe({ viewState?.onConnectivityChange(it) }, ::defaultOnError)
                .addTo(compositeDisposable)
    }
}