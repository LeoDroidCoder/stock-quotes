package com.leodroidcoder.stockqoutes.presentation.symbols.adapter

import android.view.View
import android.widget.CheckBox
import com.leodroidcoder.genericadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_symbol.view.*

class SymbolViewHolder(
        itemView: View, listener: SymbolsAdapter.ItemCheckListener?
) : BaseViewHolder<Pair<String, Boolean>, SymbolsAdapter.ItemCheckListener>(itemView) {

    private val cbSymbol: CheckBox = itemView.cb_symbol

    init {
        listener?.run {
            cbSymbol.setOnClickListener { onItemCheck(adapterPosition, cbSymbol.isChecked) }
        }
    }

    override fun onBind(item: Pair<String, Boolean>) {
        cbSymbol.text = item.first
        cbSymbol.isChecked = item.second
    }

    override fun onBind(item: Pair<String, Boolean>?, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBind(item, payloads)
        } else {
            payloads.filter { it == PAYLOAD_IS_CHECKED }
                    .forEach { cbSymbol.isChecked = item?.second == true }
        }
    }

    companion object {
        const val PAYLOAD_IS_CHECKED = "PAYLOAD_IS_CHECKED"
    }
}