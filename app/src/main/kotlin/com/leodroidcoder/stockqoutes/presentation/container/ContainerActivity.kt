package com.leodroidcoder.stockqoutes.presentation.container

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.presentation.base.BaseActivity
import com.leodroidcoder.stockqoutes.presentation.base.isBackPressedOnHolderWithId
import com.leodroidcoder.stockqoutes.presentation.chart.ChartFragment
import com.leodroidcoder.stockqoutes.presentation.navigation.Screens
import com.leodroidcoder.stockqoutes.presentation.quotes.QuotesFragment
import com.leodroidcoder.stockqoutes.presentation.symbols.SymbolsFragment
import com.leodroidcoder.stockqoutes.presentation.symbols.SymbolsPresenter
import kotlinx.android.synthetic.main.activity_container.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import javax.inject.Inject

/**
 * Container screen Activity.
 * Hosts Fragments with other screens.
 * Receives data from the presenter [SymbolsPresenter] and shows it.
 * Passes user interaction events to presenter.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class ContainerActivity : BaseActivity<ContainerMvpView, ContainerPresenter>(), ContainerMvpView {

    // Cicerone router and navigator holder
    @Inject lateinit var router: Router
    @Inject lateinit var navigatorHolder: NavigatorHolder
    @InjectPresenter lateinit var presenter: ContainerPresenter

    @ProvidePresenter
    fun providePresenter(): ContainerPresenter = lazyPresenter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onConnectivityChange(connected: Boolean) {
        toolbar?.subtitle = if (!connected) getString(R.string.text_not_connected) else ""
    }

    /**
     * Dispatch onPause() to fragments.
     */
    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
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

    /**
     * Application navigator, used for a convenient navigation between fragments.
     * Responsible for:
     * 1. Creating fragments.
     * 2. Navigating between screens.
     * 3. Showing messages.
     *
     * @since 1.0.0
     */
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
}

