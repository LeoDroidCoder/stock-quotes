package com.leodroidcoder.stockqoutes.data.sockets.exception

import com.neovisionaries.ws.client.HostnameUnverifiedException
import com.neovisionaries.ws.client.OpeningHandshakeException
import com.neovisionaries.ws.client.WebSocketException

/**
 * Exception factory.
 * Constructs a [SocketConnectionException] with a human-readable message
 * from exceptions, caused connection to sockets problems.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
object ExceptionFactory {

    fun create(cause: Throwable) = when (cause) {
        is InternetConnectionException ->
            SocketConnectionException("Internet connection problem", cause)
        is OpeningHandshakeException ->
            SocketConnectionException("A violation against the WebSocket protocol was detected during the opening handshake", cause)
        is HostnameUnverifiedException ->
            SocketConnectionException("The certificate of the peer does not match the expected hostname", cause)
        is WebSocketException ->
            SocketConnectionException("Failed to establish ask WebSocket connection", cause)
        else -> cause
    }
}