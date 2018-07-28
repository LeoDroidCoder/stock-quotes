package com.leodroidcoder.stockqoutes.presentation.quotes.adapter

import com.leodroidcoder.genericadapter.BaseDiffCallback
import com.leodroidcoder.stockqoutes.domain.entity.Tick


class QuoteDiffCallback(
        oldItems: List<Tick>,
        newItems: List<Tick>
) : BaseDiffCallback<Tick>(oldItems, newItems) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].symbol == newItems[newItemPosition].symbol

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.symbol == newItem.symbol && oldItem.ask == newItem.ask
                && oldItem.bid == newItem.bid && oldItem.spread == newItem.spread
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        val payloads = mutableListOf<String>()
        if (oldItem.symbol != newItem.symbol) {
            payloads.add(QuoteViewHolder.PAYLOAD_SYMBOL)
        }
        if (oldItem.bid != newItem.bid || oldItem.ask != newItem.ask) {
            payloads.add(QuoteViewHolder.PAYLOAD_BID_ASK)
        }
        if (oldItem.spread != newItem.spread) {
            payloads.add(QuoteViewHolder.PAYLOAD_SPREAD)
        }
        return if (payloads.isNotEmpty())
            payloads
        else
            super.getChangePayload(oldItemPosition, newItemPosition)
    }
}