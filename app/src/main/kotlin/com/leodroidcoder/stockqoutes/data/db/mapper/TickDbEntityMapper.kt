package com.leodroidcoder.stockqoutes.data.db.mapper

import com.leodroidcoder.stockqoutes.data.db.entity.TickDbEntity
import com.leodroidcoder.stockqoutes.domain.common.ObjectMapperRound
import com.leodroidcoder.stockqoutes.domain.entity.Tick

/**
 * Mapper used to transform [TickDbEntity] into domain-layer entity [Tick] and vice-versa.
 */
object TickDbEntityMapper : ObjectMapperRound<TickDbEntity, Tick> {

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