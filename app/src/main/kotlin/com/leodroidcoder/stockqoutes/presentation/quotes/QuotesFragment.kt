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
import kotlinx.android.synthetic.main.view_bar.*
import timber.log.Timber


/**
 * Created by leonid on 9/26/17.
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
         */
        fun newInstance() = QuotesFragment()

    }

    /**
     * Provide layout resource id to parent.
     *
     * @see onCreateView
     */
    override fun getLayoutResId() = R.layout.fragment_quotes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        fab_add?.setOnClickListener { presenter.onSymbolsClick() }
        btn_toolbar_back?.visibility = View.GONE
        tv_title?.text = getString(R.string.title_main_screen)
        rv_quotes?.addItemDecoration(DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL))
        rv_quotes?.setHasFixedSize(true)
        rv_quotes?.layoutManager = LinearLayoutManager(context)
        rv_quotes?.adapter = adapter
    }

    /**
     * Populate adapter with data
     */
    override fun showData(ticks: List<Tick>) {
//        adapter.updateItems(ticks)
        //todo
        Timber.d("showData $ticks")
        adapter.items = ticks
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

    /**
     * An error occurred.
     *
     * @param errorCode code
     * @see [ErrorCodes]
     */
    override fun onError(errorCode: Int) {
        //todo move this logic to presenter
        handleError(errorCode)
    }
}