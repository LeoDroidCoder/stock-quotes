package com.leodroidcoder.stockqoutes

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import com.google.gson.Gson
import com.leodroidcoder.genericadapter.BaseViewHolder
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Contains extension functions
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
inline fun <reified T> Gson.fromJson(rawJson: String): T? = fromJson(rawJson, T::class.java)

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun Completable.subscribeBy(onErrorAction: (t: Throwable) -> Unit) = subscribe({}, onErrorAction)

val BaseViewHolder<*, *>.context: Context
    get() = itemView.context

fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)