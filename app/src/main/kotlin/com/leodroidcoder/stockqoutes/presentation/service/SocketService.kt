package com.leodroidcoder.stockqoutes.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.leodroidcoder.stockqoutes.addTo
import com.leodroidcoder.stockqoutes.domain.QuotesRepository
import com.leodroidcoder.stockqoutes.domain.common.AppSchedulers
import com.leodroidcoder.stockqoutes.subscribeBy
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

/**
 *  A Service, responsible for:
 *  1. Connecting to WebSockets
 *  2. Listening for sockets updates and saving them to database.
 *  3. Disconnecting from WebSockets.
 *  Connects to sockets once is bound [onBind] and disconnects in [onUnbind].
 */
class SocketService : DaggerService() {

    @Inject
    lateinit var repository: QuotesRepository

    private val compositeDisposable = CompositeDisposable()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        connectAndSubscribeForUpdates()
        return Service.START_STICKY
    }

    /**
     * Connects to WebSockets and subscribe for updates
     */
    private fun connectAndSubscribeForUpdates() {
        repository.connectToSockets()
                .doOnComplete({
                    subscribeForSymbolUpdates()
                    subscribeForTickUpdates()
                })
                .subscribeOn(AppSchedulers.io())
                .subscribeBy(Timber::e)
                .addTo(compositeDisposable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Subscribes for symbol updates.
     * Saves them to database.
     * Subscribes on io thread [AppSchedulers.io],
     * writes to database on computation thread [AppSchedulers.computation]
     */
    private fun subscribeForSymbolUpdates() {
        repository.subscribeForSymbolUpdates()
                .observeOn(AppSchedulers.computation())
                .doOnNext { Timber.d("Quotes updated!! size=${it.ticks.size}") }
                .flatMapCompletable {
                    repository.saveSymbols(it)

                } //todo
                .subscribeOn(AppSchedulers.io())
                .subscribe(::onSymbolsSaved, Timber::e)
                .addTo(compositeDisposable)
    }

    private fun onSymbolsSaved() {
        Timber.d("Symbols were received and saved")
    }

    /**
     * Subscribes for tick updates.
     * Saves them to database.
     * Subscribes on io thread [AppSchedulers.io],
     * writes to database on computation thread [AppSchedulers.computation]
     */
    private fun subscribeForTickUpdates() {
//        repository.subscribeForTickUpdates()
//                .observeOn(AppSchedulers.computation())
//                .flatMapCompletable { repository.saveTicks(it) } //todo
//                .subscribeOn(AppSchedulers.io())
//                .subscribe(::onTicksSaved, Timber::e)
//                .addTo(compositeDisposable)
    }


    private fun onTicksSaved() {
        Timber.d("Ticks were received and saved")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Disconnect from sockets and clear disposables
        repository.disconnectFromSockets().blockingAwait()
        compositeDisposable.clear()
    }
}