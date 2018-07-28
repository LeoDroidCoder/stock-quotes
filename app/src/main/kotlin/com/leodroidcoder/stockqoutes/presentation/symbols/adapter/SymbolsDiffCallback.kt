package com.leodroidcoder.stockqoutes.presentation.symbols.adapter

import com.leodroidcoder.genericadapter.BaseDiffCallback

class SymbolsDiffCallback(
    oldItems: List<Pair<String, Boolean>>,
    newItems: List<Pair<String, Boolean>>
) : BaseDiffCallback<Pair<String, Boolean>>(oldItems, newItems) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].first == newItems[newItemPosition].first
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].second == newItems[newItemPosition].second
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        if (oldItems[oldItemPosition].second != newItems[newItemPosition].second) {
            return SymbolViewHolder.PAYLOAD_IS_CHECKED
        }
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}