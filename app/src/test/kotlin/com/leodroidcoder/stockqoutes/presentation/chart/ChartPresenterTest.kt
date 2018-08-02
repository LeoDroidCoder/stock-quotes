package com.leodroidcoder.stockqoutes.presentation.chart

import com.leodroidcoder.stockqoutes.common.BaseUnitTest
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import ru.terrakok.cicerone.Router


class ChartPresenterTest : BaseUnitTest() {

    private lateinit var presenter: ChartPresenter
    @Mock private lateinit var router: Router
    @Mock private lateinit var repository: QuotesRepository
    @Mock private lateinit var mvpView: ChartMvpView
    private val mockSymbol = "AAA"

    @Before
    override fun init() {
        super.init()
        presenter = ChartPresenter(router, repository, mockSymbol)
    }

    @Test
    fun attachView_setupToolbar() {
        doReturn(Flowable.empty<Any>()).whenever(repository).getTicks(anyString())

        presenter.attachView(mvpView)

        verify(mvpView, times(1)).setupToolbar(mockSymbol, true)
    }
}