package com.leodroidcoder.stockqoutes.presentation.quotes.adapter

import android.view.View
import android.widget.TextView
import com.leodroidcoder.genericadapter.BaseViewHolder
import com.leodroidcoder.genericadapter.OnRecyclerItemClickListener
import com.leodroidcoder.stockqoutes.R
import com.leodroidcoder.stockqoutes.context
import com.leodroidcoder.stockqoutes.domain.entity.Tick
import kotlinx.android.synthetic.main.item_quote.view.*

class QuoteViewHolder(
        itemView: View, listener: OnRecyclerItemClickListener?
) : BaseViewHolder<Tick, OnRecyclerItemClickListener>(itemView) {

    private val tvSymbol: TextView = itemView.tv_symbol
    private val tvBidAsk: TextView = itemView.tv_bid_ask
    private val tvSpread: TextView = itemView.tv_spread

    init {
        listener?.run {
            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }
    }

    override fun onBind(item: Tick) {
        tvSymbol.text = item.formattedSymbol
        tvBidAsk.text = context.getString(R.string.format_bid_ask, item.bid, item.ask)
        tvSpread.text = item.spread.toString()
    }

    override fun onBind(item: Tick, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBind(item, payloads)
        } else {
            // process payloads, grouped bui
            payloads.forEach {
                (it as? List<*>)?.let { processPayloads(it.filterIsInstance<Any>(), item) }
            }
        }
    }

    /**
     * Process payloads and update only the views which were changed
     *
     * @since 1.0.0
     */
    private fun processPayloads(payloads: List<Any>, item: Tick) {
        payloads.forEach {
            when (it) {
                PAYLOAD_SYMBOL ->
                    tvSymbol.text = item.formattedSymbol
                PAYLOAD_BID_ASK ->
                    tvBidAsk.text = context.getString(R.string.format_bid_ask, item.bid, item.ask)
                PAYLOAD_SPREAD ->
                    tvSpread.text = item.spread.toString()
            }
        }
    }

    companion object {
        const val PAYLOAD_SYMBOL = "PAYLOAD_SYMBOL"
        const val PAYLOAD_BID_ASK = "PAYLOAD_BID_ASK"
        const val PAYLOAD_SPREAD = "PAYLOAD_SPREAD"
    }
}