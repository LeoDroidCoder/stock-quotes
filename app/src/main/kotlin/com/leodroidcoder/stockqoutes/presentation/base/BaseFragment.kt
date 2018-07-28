package com.leodroidcoder.stockqoutes.presentation.base

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.leodroidcoder.stockqoutes.domain.common.ErrorCodes
import com.leodroidcoder.stockqoutes.domain.common.ErrorMessageFactory
import com.leodroidcoder.stockqoutes.presentation.common.navigation.BackButtonListener
import dagger.Lazy
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by leonid on 9/26/17.
 */
abstract class BaseFragment<V : BaseMvpView, P : BasePresenter<V>> : MvpAppCompatFragment(),
        HasFragmentInjector, BackButtonListener {

    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var lazyPresenter: Lazy<P>

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    override fun fragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    /**
     * Retrieves error message by error code and shows it.
     * Delegates to the basse activity [BaseActivity]
     *
     * @param errorCode error code
     * @see [ErrorCodes]
     * @see [ErrorMessageFactory]
     */
    protected fun handleError(errorCode: Int) {
        (activity as? BaseActivity<*, *>)?.handleError(errorCode)
    }

    /**
     * Shows Toast message.
     * Delegates to the basse activity [BaseActivity]
     *
     * @param message to show
     */
    protected fun showMessage(message: String) {
        (activity as? BaseActivity<*, *>)?.showMessage(message)
    }

}
