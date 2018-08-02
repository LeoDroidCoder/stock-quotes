package com.leodroidcoder.stockqoutes.data.sockets.entity

import com.google.gson.annotations.SerializedName
import com.leodroidcoder.stockqoutes.data.sockets.mapper.TickEntityMapper


/**
 * Network-layer Tick entity.
 *
 * @see TickEntityMapper
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
data class TickEntity(
        @SerializedName("s") val s: String,
        @SerializedName("b") val b: Double,
        @SerializedName("bf") val bf: Int,
        @SerializedName("a") val a: Double,
        @SerializedName("af") val af: Int,
        @SerializedName("spr") val spr: Double
)