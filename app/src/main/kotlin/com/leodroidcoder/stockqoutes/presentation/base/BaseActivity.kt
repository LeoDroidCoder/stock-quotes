package com.leodroidcoder.stockqoutes.presentation.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.leodroidcoder.stockqoutes.presentation.navigation.BackButtonListener
import dagger.Lazy
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ru.terrakok.cicerone.Router
import javax.inject.Inject


/**
 * Base Activity class.
 * Provides common functionality for all the Activities in application.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
abstract class BaseActivity<V : BaseMvpView, P : BasePresenter<V>> : MvpAppCompatActivity(), HasSupportFragmentInjector {

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var lazyPresenter: Lazy<P>

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}

/**
 * Navigation helper extension function.
 * Determines whether a "Back" button was pressed in a fragment container.
 *
 * @param holderId container resource id
 * @see Router
 *
 * @since 1.0.0
 */
infix fun FragmentManager.isBackPressedOnHolderWithId(@IdRes holderId: Int) =
        findFragmentById(holderId).isBackPressed()

/**
 * Fragment extension function.
 * Navigation helper function.
 * Determines whether a "Back" button was pressed in a fragment.
 *
 * @see BackButtonListener
 *
 * @since 1.0.0
 */
fun Fragment?.isBackPressed(): Boolean =
        (this as? BackButtonListener)?.onBackButtonPressed() ?: false

