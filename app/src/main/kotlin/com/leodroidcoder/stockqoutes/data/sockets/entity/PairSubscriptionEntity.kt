package com.leodroidcoder.stockqoutes.data.sockets.entity

import com.google.gson.annotations.SerializedName

class PairSubscriptionEntity(
        @SerializedName("subscribed_count") val subscribedCount: Int,
        @SerializedName("subscribed_list") val subscribedList: TicksSubscriptionsEntity
) : BaseResponse()