package com.leodroidcoder.stockqoutes.data.db.mapper

import com.leodroidcoder.stockqoutes.data.db.entity.TickDbEntity
import com.leodroidcoder.stockqoutes.domain.common.ObjectMapperRound
import com.leodroidcoder.stockqoutes.domain.entity.Tick

/**
 * Mapper used to transform database tick entity [TickDbEntity]
 * into domain-layer entity [Tick] and vice-versa.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
object TickDbEntityMapper : ObjectMapperRound<TickDbEntity, Tick> {

    /**
     * Transforms [TickDbEntity] into [Tick].
     *
     * @since 1.0.0
     */
    override fun mapTo(input: TickDbEntity): Tick {
        return Tick(
                id = input.id,
                symbol = input.symbol,
                bid = input.bid,
                bf = input.bf,
                ask = input.ask,
                af = input.af,
                spread = input.spread,
                date = input.date
        )
    }

    /**
     * Transforms [Tick] into [TickDbEntity].
     *
     * @since 1.0.0
     */
    override fun mapFrom(input: Tick): TickDbEntity {
        return TickDbEntity(
                id = input.id,
                symbol = input.symbol,
                bid = input.bid,
                bf = input.bf,
                ask = input.ask,
                af = input.af,
                spread = input.spread,
                date = input.date
        )
    }
}