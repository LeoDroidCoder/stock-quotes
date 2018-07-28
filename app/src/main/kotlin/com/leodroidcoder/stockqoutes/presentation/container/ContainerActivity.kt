package com.leodroidcoder.stockqoutes.presentation.container

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.presentation.base.BaseActivity
import com.leodroidcoder.stockqoutes.presentation.base.isBackPressedOnHolderWithId
import com.leodroidcoder.stockqoutes.presentation.chart.ChartFragment
import com.leodroidcoder.stockqoutes.presentation.common.navigation.Screens
import com.leodroidcoder.stockqoutes.presentation.quotes.QuotesFragment
import com.leodroidcoder.stockqoutes.presentation.service.SocketService
import com.leodroidcoder.stockqoutes.presentation.symbols.SymbolsFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import javax.inject.Inject

class ContainerActivity : BaseActivity<ContainerMvpView, ContainerPresenter>(), ContainerMvpView {

    // Cicerone router and navigator holder
    @Inject lateinit var router: Router
    @Inject lateinit var navigatorHolder: NavigatorHolder
    @InjectPresenter lateinit var presenter: ContainerPresenter

    @ProvidePresenter
    fun providePresenter(): ContainerPresenter = lazyPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this, SocketService::class.java))
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    /**
     * Dispatch onPause() to fragments.
     */
    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        stopService(Intent(this, SocketService::class.java))
    }

    private val navigator: Navigator by lazy {
        object : SupportFragmentNavigator(supportFragmentManager, R.id.fl_fragment_container) {
            override fun createFragment(screenKey: String?, data: Any?): Fragment =
                when (screenKey) {
                    Screens.SCREEN_QUOTES ->
                        QuotesFragment.newInstance()
                    Screens.SCREEN_SYMBOLS ->
                        SymbolsFragment.newInstance()
                    Screens.SCREEN_CHART ->
                        ChartFragment.newInstance(data as? String ?: "")

                    else -> {
                        throw IllegalArgumentException("Cannot create fragment. Illegal screen key: $screenKey")
                    }
                }

            /**
             * Called when we try to back from the root.
             */
            override fun exit() {
                router.exit()
            }

            override fun showSystemMessage(message: String?) {
                Toast.makeText(this@ContainerActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * An error occurred.
     *
     * @param errorCode code
     * @see [ErrorCodes]
     */
    override fun onError(errorCode: Int) {
        handleError(errorCode)
    }

    /**
     * Takes care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun onBackPressed() {
        if (supportFragmentManager isBackPressedOnHolderWithId R.id.fl_fragment_container) {
            return
        } else {
            super.onBackPressed()
        }
    }
}

