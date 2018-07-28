package com.leodroidcoder.stockqoutes.presentation.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import timber.log.Timber

/**
 * Created by leonid on 9/26/17.
 */
abstract class BasePresenter<V : BaseMvpView>(private val router: Router?) : MvpPresenter<V>() {

    protected val compositeDisposable = CompositeDisposable()

    override fun destroyView(view: V) {
        super.destroyView(view)
        compositeDisposable.clear()
    }

    protected fun defaultOnError(t: Throwable) {
        Timber.d(t)
    }

    /**
     * Move to the previous screen when back button was pressed.
     *
     * @since 0.1.0
     */
    fun onBackPressed() {
        router?.exit()
    }
}