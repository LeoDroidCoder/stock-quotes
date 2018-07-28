package com.leodroidcoder.stockqoutes.data.sockets.mapper

import com.leodroidcoder.stockqoutes.data.sockets.entity.TickEntity
import com.leodroidcoder.stockqoutes.domain.common.ObjectMapper
import com.leodroidcoder.stockqoutes.domain.entity.Tick

/**
 * Mapper used to transform [TickEntity] into domain-layer entity [Tick].
 */
object TickEntityMapper : ObjectMapper<TickEntity, Tick> {

    override fun mapTo(input: TickEntity): Tick {
        return Tick(
            symbol = input.s,
            bid = input.b,
            bf = input.bf,
            ask = input.a,
            af = input.af,
            spread = input.spr
        )
    }
}