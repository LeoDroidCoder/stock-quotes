package com.leodroidcoder.stockqoutes.presentation.symbols

import com.leodroidcoder.stockqoutes.common.BaseUnitTest
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import ru.terrakok.cicerone.Router
import java.util.*

class SymbolsPresenterTest : BaseUnitTest() {

    @InjectMocks private lateinit var presenter: SymbolsPresenter
    @Mock private lateinit var router: Router
    @Mock private lateinit var repository: QuotesRepository
    @Mock private lateinit var mvpView: SymbolsMvpView

    @Before
    override fun init() {
        super.init()
        presenter = SymbolsPresenter(router, repository)
    }

    @Test
    fun attachView_setupToolbar() {
        doReturn(Flowable.empty<Any>()).whenever(repository).getSymbolSubscriptions()

        presenter.attachView(mvpView)

        verify(mvpView, times(1)).setupToolbar(backEnabled = true)
    }

    @Test
    fun attachView_loadData_success() {
        val mockData = getMockSymbolSubscriptions()
        doReturn(Flowable.just(mockData)).whenever(repository).getSymbolSubscriptions()

        presenter.attachView(mvpView)

        verify(mvpView, times(1)).onSymbols(mockData)
    }

    @Test
    fun attachView_loadData_flowable() {
        val emissionsCount = 5
        val mockData = getMockSymbolSubscriptions()
        val list = Collections.nCopies(emissionsCount, mockData)

        doReturn(Flowable.fromIterable(list)).whenever(repository).getSymbolSubscriptions()

        presenter.attachView(mvpView)

        verify(mvpView, times(emissionsCount)).onSymbols(mockData)
    }

    @Test
    fun attachView_loadData_error() {
        doReturn(Flowable.error<Exception>(RuntimeException("Ups"))).whenever(repository).getSymbolSubscriptions()

        presenter.attachView(mvpView)

        verify(mvpView, never()).onSymbols(any())
    }

    @Test
    fun symbolSubscriptionCheck_subscribe() {
        doReturn(Completable.complete()).whenever(repository).subscribeForSymbol(any())

        val symbol = "AAA"
        presenter.symbolSubscriptionCheck(symbol, true)

        verify(repository, times(1)).subscribeForSymbol(symbol)
        verify(repository, never()).unsubscribeFromSymbol(symbol)
    }

    @Test
    fun symbolSubscriptionCheck_unsubscribe() {
        doReturn(Completable.complete()).whenever(repository).unsubscribeFromSymbol(any())

        val symbol = "AAA"
        presenter.symbolSubscriptionCheck(symbol, false)

        verify(repository, times(1)).unsubscribeFromSymbol(symbol)
        verify(repository, never()).subscribeForSymbol(symbol)
    }

    private fun getMockSymbolSubscriptions() = listOf(Pair("AAA", true), Pair("BBB", false))

}