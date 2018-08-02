package com.leodroidcoder.stockqoutes.data.sockets.entity

import com.google.gson.annotations.SerializedName
import com.leodroidcoder.stockqoutes.data.sockets.deserializer.ResponseDeserializer
import com.leodroidcoder.stockqoutes.data.sockets.mapper.TickEntityMapper

/**
 * WebSocket ticks response class.
 *
 * @see [ResponseDeserializer]
 * @see [TickEntityMapper]
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
data class TicksResponse(
        @SerializedName("ticks") val ticks: List<TickEntity>? = listOf()
) : BaseResponse()