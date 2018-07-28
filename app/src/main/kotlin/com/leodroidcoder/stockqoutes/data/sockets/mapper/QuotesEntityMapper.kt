package com.leodroidcoder.stockqoutes.data.sockets.mapper

import com.leodroidcoder.stockqoutes.data.sockets.entity.PairSubscriptionEntity
import com.leodroidcoder.stockqoutes.domain.common.ObjectMapper
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription

object QuotesEntityMapper : ObjectMapper<PairSubscriptionEntity, SymbolsSubscription> {

    override fun mapTo(input: PairSubscriptionEntity): SymbolsSubscription = SymbolsSubscription(
            input.subscribedCount,
            TickEntityMapper.mapTo(input.subscribedList.ticks.orEmpty())
    )
}