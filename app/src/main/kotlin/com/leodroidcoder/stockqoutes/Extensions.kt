package com.leodroidcoder.stockqoutes

import android.content.Context
import com.google.gson.Gson
import com.leodroidcoder.genericadapter.BaseViewHolder
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

inline fun <reified T> Gson.fromJson(rawJson: String): T? = fromJson(rawJson, T::class.java)

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> Collection<T>?.isNullOrEmpty(): Boolean =
        this?.let {
            isEmpty()
        } == null

fun Completable.subscribeBy(onErrorAction: (t: Throwable) -> Unit) = subscribe({}, onErrorAction)

fun Flowable<*>.subscribeBy(onErrorAction: (t: Throwable) -> Unit) = subscribe({}, onErrorAction)

val BaseViewHolder<*, *>.context: Context
    get() = itemView.context
