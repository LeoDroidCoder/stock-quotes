package com.leodroidcoder.stockqoutes.data.sockets.entity

import com.leodroidcoder.stockqoutes.data.sockets.deserializer.ResponseDeserializer
import com.leodroidcoder.stockqoutes.di.module.DataModule

/**
 * Base WebSocket response class.
 * Deserialization of child classes should be handled in [ResponseDeserializer]
 * @see [DataModule.provideGson]
 */
abstract class BaseResponse