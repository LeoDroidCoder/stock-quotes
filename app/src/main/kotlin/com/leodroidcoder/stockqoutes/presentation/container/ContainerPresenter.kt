package com.leodroidcoder.stockqoutes.presentation.container

import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.presentation.base.BasePresenter
import com.leodroidcoder.stockqoutes.presentation.common.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by leonid on 9/26/17.
 */
class ContainerPresenter @Inject constructor(
    private val router: Router,
    private val repository: QuotesRepository
) : BasePresenter<ContainerMvpView>(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        connectToSockets()
        router.navigateTo(Screens.SCREEN_QUOTES)
    }

    private fun connectToSockets() {
        //todo connect and disconnect in onstart
//        executeRequest(
//            repository.connectToSockets()
//                .doOnDispose {
//                    Timber.d("DISPOSE. Disconnecting from sockets")
//                    repository.disconnectFromSockets()
//                }
//                .subscribeOn(AppSchedulers.io())
//                .subscribe(
//                    { Timber.d("connected") },
//                    { e -> Timber.d("ERROR: $e") })
//        )
    }
}