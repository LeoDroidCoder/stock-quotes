package com.leodroidcoder.stockqoutes.presentation.quotes

import com.leodroidcoder.stockqoutes.common.BaseUnitTest
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.leodroidcoder.stockqoutes.presentation.navigation.Screens
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import ru.terrakok.cicerone.Router
import java.util.*

class QuotesPresenterTest : BaseUnitTest() {

    @InjectMocks private lateinit var presenter: QuotesPresenter
    @Mock private lateinit var router: Router
    @Mock private lateinit var repository: QuotesRepository
    @Mock private lateinit var mvpView: QuotesMvpView

    @Before
    override fun init() {
        super.init()
        presenter = QuotesPresenter(router, repository)
    }

    @Test
    fun attachView_setupToolbar() {
        doReturn(Flowable.empty<Any>()).whenever(repository).getLastSymbolTicks()
        presenter.attachView(mvpView)

        verify(mvpView, times(1)).setupToolbar()
    }

    @Test
    fun onSymbolsClick_navigateToSymbolsScreen() {
        presenter.onSymbolsClick()

        verify(router, times(1)).navigateTo(Screens.SCREEN_SYMBOLS)
    }

    @Test
    fun onItemClick_navigateToChartScreen() {
        val symbol = "EURUSD"
        presenter.onItemClick(symbol)

        verify(router, times(1)).navigateTo(Screens.SCREEN_CHART, symbol)
    }

    @Test
    fun loadData_success() {
        val mockTicks = getMockTicks()
        doReturn(Flowable.just(mockTicks)).whenever(repository).getLastSymbolTicks()

        presenter.attachView(mvpView)

        verify(mvpView, times(1)).showData(mockTicks)
    }

    @Test
    fun loadData_success_flowable() {
        val emissionsCount = 5
        val mockData = getMockTicks()
        val list = Collections.nCopies(emissionsCount, mockData)

        doReturn(Flowable.fromIterable(list)).whenever(repository).getLastSymbolTicks()

        presenter.attachView(mvpView)

        verify(mvpView, times(emissionsCount)).showData(mockData)
    }

    @Test
    fun loadData_error() {
        val mockTicks = getMockTicks()
        doReturn(Flowable.error<RuntimeException>(RuntimeException())).whenever(repository).getLastSymbolTicks()

        presenter.attachView(mvpView)

        verify(mvpView, never()).showData(mockTicks)
    }

    @Test
    fun onSymbolsOrderCheck_descendingOrder() {
        val mockTicks = getMockTicks()
        doReturn(Flowable.just(mockTicks)).whenever(repository).getLastSymbolTicks()

        presenter.attachView(mvpView)
        presenter.onSymbolsOrderCheck(true)

        verify(mvpView, times(1)).showData(mockTicks.reversed())
    }

    @Test
    fun onSymbolsOrderCheck_ascendingOrder() {
        val mockTicks = getMockTicks()
        doReturn(Flowable.just(mockTicks)).whenever(repository).getLastSymbolTicks()

        presenter.attachView(mvpView)
        presenter.onSymbolsOrderCheck(false)

        verify(mvpView, times(2)).showData(mockTicks)
    }

    private fun getMockTicks(): List<Tick> = listOf(
            Tick(10L, "AAA", .0, 0, .0, 0, .0),
            Tick(20L, "BBB", .1, 1, .1, 1, .1)
    )
}