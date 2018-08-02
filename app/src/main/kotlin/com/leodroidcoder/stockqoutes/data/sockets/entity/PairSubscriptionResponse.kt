package com.leodroidcoder.stockqoutes.data.sockets.entity

import com.google.gson.annotations.SerializedName
import com.leodroidcoder.stockqoutes.data.sockets.deserializer.ResponseDeserializer
import com.leodroidcoder.stockqoutes.data.sockets.mapper.QuotesEntityMapper

/**
 * WebSocket response class.
 * Represents subscription/unsubscription to currency symbols result.
 *
 * @see [ResponseDeserializer]
 * @see [QuotesEntityMapper]
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
data class PairSubscriptionResponse(
        @SerializedName("subscribed_count") val subscribedCount: Int,
        @SerializedName("subscribed_list") val subscribedList: TicksResponse
) : BaseResponse()