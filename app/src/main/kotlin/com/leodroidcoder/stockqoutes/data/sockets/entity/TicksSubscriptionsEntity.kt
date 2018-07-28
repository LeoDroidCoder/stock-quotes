package com.leodroidcoder.stockqoutes.data.sockets.entity

import com.google.gson.annotations.SerializedName

data class TicksSubscriptionsEntity(
        @SerializedName("ticks") val ticks: List<TickEntity>? = listOf()
) : BaseResponse()