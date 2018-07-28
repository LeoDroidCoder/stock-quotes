package com.leodroidcoder.stockqoutes.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.leodroidcoder.stockqoutes.data.db.entity.TickDbEntity
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import io.reactivex.Flowable

@Dao
abstract class TickDao {

    /**
     * Insert a Tick entity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTick(tick: TickDbEntity)

    /**
     * Insert a list of Tick entities
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTicks(ticks: List<TickDbEntity>)

    /**
     * Get ticks of a specific symbol from database.
     * Emits fresh data whenever it is changed in db.
     *
     * @param symbol currency pair symbol, for example "EURUSD"
     */
    @Query("SELECT * FROM tick WHERE symbol LIKE :symbol")
    abstract fun getTicks(symbol: String): Flowable<List<TickDbEntity>>

    // todo stopshp temp
    @Query("SELECT * FROM tick")
    abstract fun getAllTicks(): Flowable<List<TickDbEntity>>

    /**
     * Delete all ticks, except ticks with symbols in [retainQuotes]
     *
     * @param retainQuotes List of currency pair symbol, for example "EURUSD",
     * which ticks will not be deleted
     */
    @Query("DELETE FROM tick WHERE symbol NOT IN (:retainQuotes)")
    abstract fun retainQuotes(retainQuotes: List<String>)

    /**
     * Get List of last ticks for every currency pair symbol.
     * For instance, if we subscribed for "EURUSD" tick updates, the result will contain
     * a List with a single [Tick] item, which is the last received one.
     * So we get unique symbols with the last ticks.
     */
    @Query("SELECT *, MAX (date) FROM tick GROUP BY symbol")
    abstract fun getLastSymbolTicks(): Flowable<List<TickDbEntity>>

    /**
     * Get list of unique symbols (currency pairs) [TickDbEntity.symbol]
     */
    @Query("SELECT DISTINCT symbol FROM tick ORDER BY symbol ASC")
    abstract fun getSubscribedPairs(): Flowable<List<String>>
}