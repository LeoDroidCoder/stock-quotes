package com.leodroidcoder.stockqoutes.data.sockets.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.leodroidcoder.stockqoutes.data.sockets.entity.BaseResponse
import com.leodroidcoder.stockqoutes.data.sockets.entity.PairSubscriptionResponse
import com.leodroidcoder.stockqoutes.data.sockets.entity.TicksResponse
import java.lang.reflect.Type

/**
 * WebSockets response parser.
 * Parses raw json into objects
 *
 * @see BaseResponse
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class ResponseDeserializer : JsonDeserializer<BaseResponse> {

    @Throws(JsonParseException::class)
    override fun deserialize(
            json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): BaseResponse {
        val jsonObject = json.asJsonObject
        // would be better to distinguish response by kind of "type" parameter, but for now use this way
        val subscribedCount = jsonObject.get("subscribed_count")
        return if (subscribedCount != null) {
            context.deserialize<BaseResponse>(jsonObject, PairSubscriptionResponse::class.java)
        } else {
            context.deserialize<BaseResponse>(jsonObject, TicksResponse::class.java)
        }
    }
}