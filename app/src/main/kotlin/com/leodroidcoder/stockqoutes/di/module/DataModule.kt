package com.leodroidcoder.stockqoutes.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.leodroidcoder.stockqoutes.BuildConfig
import com.leodroidcoder.stockqoutes.data.db.AppDataBase
import com.leodroidcoder.stockqoutes.data.sockets.WebSocketsManager
import com.leodroidcoder.stockqoutes.data.sockets.deserializer.ResponseDeserializer
import com.leodroidcoder.stockqoutes.data.sockets.entity.BaseResponse
import com.leodroidcoder.stockqoutes.di.qualifier.AppContext
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Data module.
 * Used to provide data-related objects.
 *
 * @author Leonid Ustenko (Leo.Droidcoder@gmail.com)
 * @since 1.0.0
 */
@Module
class DataModule {

    companion object {
        private const val CONNECTION_TIMEOUT = 5000
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
            .registerTypeAdapter(BaseResponse::class.java, ResponseDeserializer())
            .create()

    @Singleton
    @Provides
    fun provideWebSocketClient(): WebSocket = WebSocketFactory()
            .setConnectionTimeout(CONNECTION_TIMEOUT)
            .createSocket(BuildConfig.WEBSOCKET_HOST)

    @Singleton
    @Provides
    fun provideWebSocketsManager(client: WebSocket, gson: Gson) = WebSocketsManager(client, gson)

    @Provides
    @Singleton
    fun provideDataBase(@AppContext context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, "stock_quotes")
                    .apply { if (BuildConfig.DEBUG) fallbackToDestructiveMigration() }
                    .build()
}