package com.leodroidcoder.stockqoutes.presentation.quotes.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.leodroidcoder.genericadapter.GenericRecyclerViewAdapter
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.domain.entity.Tick

class QuoteAdapter(context: Context?, listener: OnRecyclerItemClickListener) : GenericRecyclerViewAdapter<Tick,
        OnRecyclerItemClickListener, QuoteViewHolder>(context, listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(inflate(R.layout.item_quote, parent), listener)
    }

    /**
     * Perform smart items update
     */
    override fun updateItems(newItems: List<Tick>) {
        val result = DiffUtil.calculateDiff(QuoteDiffCallback(items, newItems))
        result.dispatchUpdatesTo(this)
        super.updateItems(newItems)
    }
}