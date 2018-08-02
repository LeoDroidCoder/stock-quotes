package com.leodroidcoder.stockqoutes.presentation.symbols.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.leodroidcoder.genericadapter.BaseRecyclerListener
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.stockqoutes.R

/**
 * Symbols adapter.
 * Performs efficient item updates wit use of DiffUtil and payloads.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
class SymbolsAdapter(context: Context?, listener: SymbolsAdapter.ItemCheckListener) : GenericRecyclerViewAdapter<Pair<String, Boolean>,
            SymbolsAdapter.ItemCheckListener, SymbolViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder {
        return SymbolViewHolder(inflate(R.layout.item_symbol, parent), listener)
    }

    /**
     * Perform smart items update
     *
     * @since 1.0.0
     */
    override fun updateItems(newItems: List<Pair<String, Boolean>>) {
        val result = DiffUtil.calculateDiff(SymbolsDiffCallback(items, newItems), false)
        super.updateItems(newItems)
        result.dispatchUpdatesTo(this)
    }

    /**
     * Interface used to allow the creator to run some code when an
     * item is checked.
     *
     * @since 1.0.0
     */
    interface ItemCheckListener : BaseRecyclerListener {
        /**
         * This method will be invoked when a checkbox in the item has ben checked.
         *
         * @param position position oft he checked item
         * @param checked `true` if checked and `false` o
         *
         * @since 1.0.0
         */
        fun onItemCheck(position: Int, checked: Boolean)
    }
}
