package com.leodroidcoder.stockqoutes.presentation.quotes

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.presentation.base.BaseFragment
import com.leodroidcoder.stockqoutes.presentation.quotes.adapter.QuoteAdapter
import kotlinx.android.synthetic.main.fragment_quotes.*


/**
 * Symbols screen Fragment.
 * Shows summary of the currency pairs, for which updates user is subscribed.
 *
 * Receives data from the presenter [QuotesPresenter] and shows it.
 * Passes user interaction events to presenter.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class QuotesFragment : BaseFragment<QuotesMvpView, QuotesPresenter>(), QuotesMvpView, OnRecyclerItemClickListener {

    @InjectPresenter
    lateinit var presenter: QuotesPresenter
    private val adapter: QuoteAdapter by lazy { QuoteAdapter(context, this) }

    @ProvidePresenter
    fun providePresenter(): QuotesPresenter = lazyPresenter.get()

    companion object {

        /**
         * Returns new instance of current fragment.
         * Supply it with the arguments if needed.
         *
         * @return new instance of current fragment.
         *
         * @since 1.0.0
         */
        fun newInstance() = QuotesFragment()
    }

    /**
     * Provide layout resource id to parent.
     *
     * @see onCreateView
     *
     * @since 1.0.0
     */
    override fun getLayoutResId() = R.layout.fragment_quotes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        fab_add?.setOnClickListener { presenter.onSymbolsClick() }
        cb_symbol?.setOnClickListener { presenter.onSymbolsOrderCheck(cb_symbol.isChecked) }
        rv_quotes?.addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
        rv_quotes?.setHasFixedSize(true)
        rv_quotes?.layoutManager = LinearLayoutManager(context)
        rv_quotes?.adapter = adapter
    }

    override fun setupToolbar(title: String, backEnabled: Boolean) {
        setToolbar(getString(R.string.app_name), backEnabled)
    }

    /**
     * Populate adapter with data.
     *
     * @since 1.0.0
     */
    override fun showData(ticks: List<Tick>) {
        checkEmptyViewAppearance(ticks.isEmpty())
        adapter.updateItems(ticks)
    }

    private fun checkEmptyViewAppearance(empty: Boolean) {
        gr_recycler?.visibility = if (empty) View.GONE else View.VISIBLE
        tv_empty?.visibility = if (empty) View.VISIBLE else View.GONE
    }

    /**
     * A quote item has been clicked
     */
    override fun onItemClick(position: Int) {
        presenter.onItemClick(adapter.getItem(position).symbol)
    }

    /**
     * Callback, called on back button action in parent activity
     *
     * @since 0.1.0
     * @return true if callback should be passed to next attached fragment
     */
    override fun onBackButtonPressed(): Boolean {
        presenter.onBackPressed()
        return false
    }
}