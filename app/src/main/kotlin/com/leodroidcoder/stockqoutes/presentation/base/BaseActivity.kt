package com.leodroidcoder.stockqoutes.presentation.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.leodroidcoder.stockqoutes.domain.common.ErrorCodes
import com.leodroidcoder.stockqoutes.domain.common.ErrorMessageFactory
import com.leodroidcoder.stockqoutes.presentation.common.navigation.BackButtonListener
import dagger.Lazy
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


/**
 * Created by leonid on 9/26/17.
 */
abstract class BaseActivity<V : BaseMvpView, P : BasePresenter<V>> : MvpAppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var lazyPresenter: Lazy<P>

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    /**
     * Retrieves error message by error code and shows it.
     *
     * @param errorCode error code
     * @see [ErrorCodes]
     * @see [ErrorMessageFactory]
     */
    fun handleError(errorCode: Int) {
        //todo remove to data and replace with exception
        showMessage(ErrorMessageFactory.create(this, if (isOnline()) errorCode else ErrorCodes.ERROR_INTERNET_CONNECTION))
    }

    /**
     * Shows Toast message.
     *
     * @param message to show
     */
    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return cm?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }
}

infix fun FragmentManager.isBackPressedOnHolderWithId(holderId: Int) =
        findFragmentById(holderId).isBackPressed()

fun Fragment?.isBackPressed(): Boolean =
        (this as? BackButtonListener)?.onBackButtonPressed() ?: false

