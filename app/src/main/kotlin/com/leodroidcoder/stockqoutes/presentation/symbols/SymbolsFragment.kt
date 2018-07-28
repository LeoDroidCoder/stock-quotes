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
import kotlinx.android.synthetic.main.view_bar.*
import timber.log.Timber

/**
 * Created by leonid on 9/26/17.
 */
class SymbolsFragment : BaseFragment<SymbolsMvpView, SymbolsPresenter>(), SymbolsMvpView,
    SymbolsAdapter.ItemCheckListener {

    @InjectPresenter lateinit var presenter: SymbolsPresenter

    lateinit var adapter: SymbolsAdapter

    companion object {

        /**
         * Returns new instance of current fragment.
         * Supply it with the arguments if needed.
         *
         * @since 0.1.0
         *
         * @return new instance of current fragment.
         */
        fun newInstance() = SymbolsFragment()
    }

    @ProvidePresenter
    fun providePresenter(): SymbolsPresenter = lazyPresenter.get()

    override fun getLayoutResId() =  R.layout.fragment_symbols

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SymbolsAdapter(context, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        btn_toolbar_back?.setOnClickListener { presenter.onBackPressed() }
        tv_title?.text = getString(R.string.title_details_screen)
        rv_symbols?.addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
        rv_symbols?.setHasFixedSize(true)
        rv_symbols?.layoutManager = LinearLayoutManager(context)
        rv_symbols?.adapter = adapter
    }

    override fun onSymbols(symbols: List<Pair<String, Boolean>>) {
        Timber.d("onSymbols $symbols")
        adapter.updateItems(symbols)
    }

    override fun onItemCheck(position: Int, checked: Boolean) {
        presenter.symbolSubscriptionToggle(adapter.getItem(position).first, checked)
    }

    override fun onBackButtonPressed(): Boolean {
        presenter.onBackPressed()
        return true
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
}