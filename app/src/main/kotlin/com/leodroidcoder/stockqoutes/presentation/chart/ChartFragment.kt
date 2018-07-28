package com.leodroidcoder.stockqoutes.presentation.chart

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.view_bar.*

/**
 * Created by leonid on 9/26/17.
 */
class ChartFragment : BaseFragment<ChartMvpView, ChartPresenter>(), ChartMvpView {

    @InjectPresenter lateinit var presenter: ChartPresenter

    @ProvidePresenter
    fun providePresenter(): ChartPresenter = lazyPresenter.get()


    companion object {

        const val ARGUMENT_SYMBOL = "ARGUMENT_SYMBOL"

        /**
         * Returns new instance of current fragment.
         * Supply it with the arguments if needed.
         *
         * @since 0.1.0
         *
         * @return new instance of current fragment.
         */
        fun newInstance(symbol: String) = ChartFragment()
                .apply {
                    val args = Bundle()
                    args.putString(ARGUMENT_SYMBOL, symbol)
                    arguments = args
                }
    }

    override fun getLayoutResId() =  R.layout.fragment_chart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        btn_toolbar_back?.setOnClickListener { presenter.onBackPressed() }
        tv_title?.text = getString(R.string.title_details_screen)
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