package com.leodroidcoder.stockqoutes.presentation.base

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.leodroidcoder.stockqoutes.presentation.navigation.BackButtonListener
import dagger.Lazy
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Base Fragment class.
 * Provides common functionality for all the Fragments in application.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
abstract class BaseFragment<V : BaseMvpView, P : BasePresenter<V>> : MvpAppCompatFragment(),
        HasFragmentInjector, BackButtonListener {

    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var lazyPresenter: Lazy<P>

    @LayoutRes protected abstract fun getLayoutResId(): Int

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

    protected fun setToolbar(toolbarTitle: String, backEnabled: Boolean) {
        (activity as? AppCompatActivity)?.supportActionBar?.run {
            title = toolbarTitle
            setDisplayHomeAsUpEnabled(backEnabled)
        }
    }

    override fun onBackButtonPressed(): Boolean {
        lazyPresenter.get().onBackPressed()
        return true
    }
}
