package com.leodroidcoder.stockqoutes.domain

import com.leodroidcoder.stockqoutes.common.BaseUnitTest
import com.leodroidcoder.stockqoutes.data.db.AppDataBase
import com.leodroidcoder.stockqoutes.data.db.TickDao
import com.leodroidcoder.stockqoutes.data.db.mapper.TickDbEntityMapper
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import java.util.*

class QuotesRepositoryTest : BaseUnitTest() {

    private lateinit var repository: QuotesRepositoryImpl
    @Mock private lateinit var socketsApi: WebSocketsApi
    @Mock private lateinit var dataBase: AppDataBase
    @Mock private lateinit var tickDao: TickDao

    @Before
    override fun init() {
        super.init()
        repository = QuotesRepositoryImpl(socketsApi, dataBase)
    }

    @Test
    fun subscribeForConnectionChanges_success() {
        whenever(socketsApi.subscribeForConnectionChanges()).thenReturn(Flowable.fromIterable(listOf(true, false, true)))

        repository.subscribeForConnectivityChanges()
                .test()
                .assertNoErrors()
                .assertValueCount(3)
                .assertValueSequence(listOf(true, false, true))

        verify(socketsApi, times(1)).subscribeForConnectionChanges()
    }

    @Test
    fun subscribeForConnectionChanges_error() {
        whenever(socketsApi.subscribeForConnectionChanges()).thenReturn(Flowable.error(RuntimeException()))

        repository.subscribeForConnectivityChanges()
                .test()
                .assertFailure(RuntimeException::class.java)
                .assertValueCount(0)
    }

    /**
     * Should not emit similar values
     *
     * @since 1.0.0
     */
    @Test
    fun getTicks_debounce() {
        val mockTicks = getMockTicks()
        val symbol = mockTicks[0].symbol
        val dbTicks = TickDbEntityMapper.mapFrom(mockTicks)
        val emission = Collections.nCopies(5, dbTicks)

        whenever(dataBase.tickDao()).thenReturn(tickDao)
        whenever(tickDao.getTicks(anyString())).thenReturn(Flowable.fromIterable(emission))

        repository.getTicks(symbol)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(mockTicks)

        verify(tickDao, times(1)).getTicks(symbol)
    }

    /**
     * Should emit sequence of different values.
     *
     * @since 1.0.0
     */
    @Test
    fun getTicks_multipleEmits() {
        val ticks1 = getMockTicks()
        val ticks2 = listOf(Tick(100L, "USDEUR", .1,1,.1,1,.1))
        val dbTicks1 = TickDbEntityMapper.mapFrom(ticks1)
        val dbTicks2 = TickDbEntityMapper.mapFrom(ticks2)
        val emission = listOf(dbTicks1, dbTicks2)

        val symbol = ticks1[0].symbol

        whenever(dataBase.tickDao()).thenReturn(tickDao)
        whenever(tickDao.getTicks(anyString())).thenReturn(Flowable.fromIterable(emission))

        repository.getTicks(symbol)
                .test()
                .assertNoErrors()
                .assertValueCount(2)

        verify(tickDao, times(1)).getTicks(symbol)
    }

    @Test
    fun getTicks_error() {
        whenever(dataBase.tickDao()).thenReturn(tickDao)
        whenever(tickDao.getTicks(anyString())).thenReturn(Flowable.error(RuntimeException()))

        repository.getTicks("Ups..")
                .test()
                .assertFailure(RuntimeException::class.java)
                .assertValueCount(0)

        verify(tickDao, times(1)).getTicks(anyString())
    }

    @Test
    fun subscribeForSymbol_success() {
        whenever(socketsApi.subscribeForPairs(anyList())).thenReturn(Completable.complete())

        val symbol = "EURUSD"
        repository.subscribeForSymbol(symbol)
                .test()
                .assertNoErrors()
                .assertComplete()

        verify(socketsApi, times(1)).subscribeForPairs(listOf(symbol))
    }

    @Test
    fun subscribeForSymbol_error() {
        whenever(socketsApi.subscribeForPairs(anyList())).thenReturn(Completable.error(IllegalStateException()))

        val symbol = "EURUSD"
        repository.subscribeForSymbol(symbol)
                .test()
                .assertFailure(IllegalStateException::class.java)
                .assertNotComplete()

        verify(socketsApi, times(1)).subscribeForPairs(listOf(symbol))
    }

    @Test
    fun unSubscribeFromSymbol_success() {
        whenever(socketsApi.unSubscribeFromPairs(anyList())).thenReturn(Completable.complete())

        val symbol = "EURUSD"
        repository.unsubscribeFromSymbol(symbol)
                .test()
                .assertNoErrors()
                .assertComplete()

        verify(socketsApi, times(1)).unSubscribeFromPairs(listOf(symbol))
    }

    @Test
    fun unSubscribeFromSymbol_error() {
        whenever(socketsApi.unSubscribeFromPairs(anyList())).thenReturn(Completable.error(IllegalStateException()))

        val symbol = "EURUSD"
        repository.unsubscribeFromSymbol(symbol)
                .test()
                .assertFailure(IllegalStateException::class.java)
                .assertNotComplete()

        verify(socketsApi, times(1)).unSubscribeFromPairs(listOf(symbol))
    }

    @Test
    fun subscribeForSymbolUpdates_success() {
        val subscription = SymbolsSubscription(1, getMockTicks())
        whenever(socketsApi.subscribeForQuoteUpdates()).thenReturn(Flowable.just(subscription))

        repository.subscribeForQuotesUpdates()
                .test()
                .assertValues(subscription)
                .assertNoErrors()
    }

    @Test
    fun subscribeForSymbolUpdates_flowable() {
        val subscription1 = SymbolsSubscription(1, getMockTicks())
        val subscription2 = SymbolsSubscription(20, getMockTicks())
        val multiple = Collections.nCopies(5, subscription1).plus(subscription2)

        whenever(socketsApi.subscribeForQuoteUpdates()).thenReturn(Flowable.fromIterable(multiple))

        repository.subscribeForQuotesUpdates()
                .test()
                .assertValueAt(4) { it == subscription1 }
                .assertValueAt(5) { it == subscription2 }
                .assertValueCount(6)
                .assertNoErrors()
    }

    @Test
    fun subscribeForSymbolUpdates_notEmit() {
        whenever(socketsApi.subscribeForQuoteUpdates()).thenReturn(Flowable.never())

        repository.subscribeForQuotesUpdates()
                .test()
                .assertEmpty()
                .assertNoErrors()
    }

    @Test
    fun subscribeForSymbolUpdates_error() {
        whenever(socketsApi.subscribeForQuoteUpdates()).thenReturn(Flowable.error(RuntimeException()))

        repository.subscribeForQuotesUpdates()
                .test()
                .assertFailure(RuntimeException::class.java)
    }

    @Test
    fun getLastSymbolTicks_success() {
        val ticks = getMockTicks()
        val dbTicks = TickDbEntityMapper.mapFrom(ticks)
        // simulate multiple similar emissions from database
        val dbTicksEmissions = Collections.nCopies(5, dbTicks)

        whenever(dataBase.tickDao()).thenReturn(tickDao)
        whenever(tickDao.getLastSymbolTicks()).thenReturn(Flowable.fromIterable(dbTicksEmissions))

        repository.getLastSymbolTicks()
                .test()
                .assertNoErrors()
                .assertValue(ticks)
                .assertValueCount(1)

        verify(dataBase.tickDao(), times(1)).getLastSymbolTicks()
    }

    @Test
    fun getLastSymbolTicks_error() {
        whenever(dataBase.tickDao()).thenReturn(tickDao)
        whenever(tickDao.getLastSymbolTicks()).thenReturn(Flowable.error(RuntimeException()))

        repository.getLastSymbolTicks()
                .test()
                .assertFailure(RuntimeException::class.java)

        verify(dataBase.tickDao(), times(1)).getLastSymbolTicks()
    }

    @Test
    fun subscribeForTickUpdates_success() {
        val ticks = getMockTicks()
        val multipleEmissions = Collections.nCopies(5, ticks)

        whenever(socketsApi.subscribeForTickUpdates()).thenReturn(Flowable.fromIterable(multipleEmissions))

        repository.subscribeForTickUpdates()
                .test()
                .assertNoErrors()
                .assertValueCount(5)

        verify(socketsApi, times(1)).subscribeForTickUpdates()
    }

    @Test
    fun subscribeForTickUpdates_error() {
        whenever(socketsApi.subscribeForTickUpdates()).thenReturn(Flowable.error(RuntimeException()))

        repository.subscribeForTickUpdates()
                .test()
                .assertFailure(RuntimeException::class.java)

        verify(socketsApi, times(1)).subscribeForTickUpdates()
    }

    private fun getMockTicks(): List<Tick> = listOf(
            Tick(10L, "EURUSD", .0, 0, .0, 0, .0),
            Tick(20L, "EURAUD", .1, 1, .1, 1, .1)
    )
}