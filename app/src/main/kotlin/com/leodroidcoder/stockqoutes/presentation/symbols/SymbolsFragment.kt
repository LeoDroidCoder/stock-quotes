package com.leodroidcoder.stockqoutes.presentation.symbols

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.presentation.base.BaseFragment
import com.leodroidcoder.stockqoutes.presentation.symbols.adapter.SymbolsAdapter
import kotlinx.android.synthetic.main.fragment_symbols.*

/**
 * Symbols screen Fragment.
 * Shows available currency symbols,
 * lets user to subscribe/unsubscribe from a mentioned currencies tick updates
 *
 * Receives data from the presenter [SymbolsPresenter] and shows it.
 * Passes user interaction events to presenter.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class SymbolsFragment : BaseFragment<SymbolsMvpView, SymbolsPresenter>(), SymbolsMvpView,
        SymbolsAdapter.ItemCheckListener {

    @InjectPresenter lateinit var presenter: SymbolsPresenter

    private lateinit var adapter: SymbolsAdapter

    companion object {

        /**
         * Returns new instance of current fragment.
         * Supply it with the arguments if needed.
         *
         * @return new instance of current fragment.
         *
         * @since 0.1.0
         */
        fun newInstance() = SymbolsFragment()
    }

    @ProvidePresenter
    fun providePresenter(): SymbolsPresenter = lazyPresenter.get()

    override fun getLayoutResId() = R.layout.fragment_symbols

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SymbolsAdapter(context, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        rv_symbols?.addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
        rv_symbols?.setHasFixedSize(true)
        rv_symbols?.layoutManager = LinearLayoutManager(context)
        rv_symbols?.adapter = adapter
    }

    override fun setupToolbar(title: String, backEnabled: Boolean) {
        setToolbar(getString(R.string.title_symbols), backEnabled)
    }

    /**
     * Populate adapter with data.
     *
     * @since 1.0.0
     */
    override fun onSymbols(symbols: List<Pair<String, Boolean>>) {
        adapter.updateItems(symbols)
    }

    override fun onItemCheck(position: Int, checked: Boolean) {
        presenter.symbolSubscriptionCheck(adapter.getItem(position).first, checked)
    }
}