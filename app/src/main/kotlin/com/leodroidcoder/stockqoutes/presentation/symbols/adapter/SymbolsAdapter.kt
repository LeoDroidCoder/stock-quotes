package com.leodroidcoder.stockqoutes.presentation.symbols.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.leodroidcoder.genericadapter.BaseRecyclerListener
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.stockqoutes.R

class SymbolsAdapter(context: Context?, listener: SymbolsAdapter.ItemCheckListener) : GenericRecyclerViewAdapter<Pair<String, Boolean>,
            SymbolsAdapter.ItemCheckListener, SymbolViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder {
        return SymbolViewHolder(inflate(R.layout.item_symbol, parent), listener)
    }

    interface ItemCheckListener : BaseRecyclerListener {

        fun onItemCheck(position: Int, checked: Boolean)
    }
    /**
     * Perform smart items update
     */
    override fun updateItems(newItems: List<Pair<String, Boolean>>) {
        val result = DiffUtil.calculateDiff(SymbolsDiffCallback(items, newItems), false)
        super.updateItems(newItems)
        result.dispatchUpdatesTo(this)
    }
}
