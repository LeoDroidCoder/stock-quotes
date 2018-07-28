package com.leodroidcoder.stockqoutes.data.sockets

import com.leodroidcoder.stockqoutes.data.sockets.exception.ExceptionFactory
import com.neovisionaries.ws.client.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketsClient @Inject constructor(private val webSocket: WebSocket) {

    private val messagesProcessor = PublishProcessor.create<String>()
    private val webSocketAdapter = object : WebSocketAdapter() {
        override fun onConnected(websocket: WebSocket?, headers: MutableMap<String, MutableList<String>>?) {
            super.onConnected(websocket, headers)
            Timber.d("Connected to sockets")
        }

        override fun onTextMessage(websocket: WebSocket?, text: String?) {
            Timber.d("Received message: $text")
            messagesProcessor.onNext(text)
        }

        override fun onDisconnected(
                websocket: WebSocket?, serverCloseFrame: WebSocketFrame?,
                clientCloseFrame: WebSocketFrame?, closedByServer: Boolean
        ) {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer)
            Timber.d("Disconnected from sockets")
        }

        override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
            super.onError(websocket, cause)
            Timber.e(cause, "WebSockets error")
            webSocket.recreate().connect()
        }
    }

    fun connect(): Completable = when (webSocket.state) {
        WebSocketState.CREATED -> connectInternal()
        WebSocketState.CLOSED -> reconnect()
        WebSocketState.CLOSING -> reconnect()
        else -> Completable.complete()
    }

    private fun connectInternal(): Completable = Completable.fromAction {
        webSocket.addListener(webSocketAdapter)
        webSocket.connect()
    }.onErrorResumeNext {
        Completable.error(ExceptionFactory.create(it))
    }

    private fun reconnect(): Completable = Completable.fromAction {
        webSocket.recreate().connect()
    }

    fun send(s: String): Completable = Completable.fromAction {
        Timber.d("Sending message: $s")
        webSocket.sendText(s)
    }

    fun subscribeForMessages(): Flowable<String> = messagesProcessor.distinctUntilChanged()

    fun disconnect(): Completable = when (webSocket.state) {
        WebSocketState.CLOSED, WebSocketState.CLOSING -> Completable.complete()

        else -> Completable.fromAction {
            Timber.d("Disconnecting sockets client")
            webSocket.disconnect()
            webSocket.removeListener(webSocketAdapter)
        }
    }

    companion object {
        const val CONNECTION_TIMEOUT = 5000
    }
}
