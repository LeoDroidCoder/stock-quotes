package com.leodroidcoder.stockqoutes.presentation.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import timber.log.Timber

/**
 * Base Presenter class.
 * Provides common functionality for all the Presenters in the application.
 *
 * @see BaseMvpView
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
abstract class BasePresenter<V : BaseMvpView>(private val router: Router?) : MvpPresenter<V>() {

    /**
     * Is cleared in [detachView].
     * You should correspondingly add disposables in [attachView]
     *
     * @since 1.0.0
     */
    protected val compositeDisposable = CompositeDisposable()

    override fun attachView(view: V) {
        super.attachView(view)
        setTitle()
    }

    /**
     * Default implementation.
     * Override it in a subclass in order to pass title from presenter
     *
     * @since 1.0.0
     */
    protected open fun setTitle() {
        viewState?.setupToolbar()
    }

    override fun detachView(view: V) {
        super.detachView(view)
        compositeDisposable.clear()
    }

    /**
     * Default onError implementation.
     * Prints error stacktrace.
     *
     * @since 1.0.0
     */
    protected fun defaultOnError(t: Throwable) {
        Timber.d(t)
    }

    /**
     * Move to the previous screen when back button was pressed.
     *
     * @since 1.0.0
     */
    fun onBackPressed() {
        router?.exit()
    }
}