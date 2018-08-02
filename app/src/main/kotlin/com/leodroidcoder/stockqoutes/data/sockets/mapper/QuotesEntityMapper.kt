package com.leodroidcoder.stockqoutes.data.sockets.mapper

import com.leodroidcoder.stockqoutes.data.sockets.entity.PairSubscriptionResponse
import com.leodroidcoder.stockqoutes.domain.common.ObjectMapper
import com.leodroidcoder.stockqoutes.domain.entity.SymbolsSubscription

/**
 * Mapper used to transform response [PairSubscriptionResponse]
 * into domain-layer entity [SymbolsSubscription].
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
object QuotesEntityMapper : ObjectMapper<PairSubscriptionResponse, SymbolsSubscription> {

    override fun mapTo(input: PairSubscriptionResponse): SymbolsSubscription = SymbolsSubscription(
            input.subscribedCount,
            TickEntityMapper.mapTo(input.subscribedList.ticks.orEmpty())
    )
}