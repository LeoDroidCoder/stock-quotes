package com.leodroidcoder.stockqoutes.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.leodroidcoder.stockqoutes.BuildConfig
import com.leodroidcoder.stockqoutes.data.db.AppDataBase
import com.leodroidcoder.stockqoutes.data.sockets.WebSocketsClient
import com.leodroidcoder.stockqoutes.data.sockets.WebSocketsManager
import com.leodroidcoder.stockqoutes.data.sockets.deserializer.ResponseDeserializer
import com.leodroidcoder.stockqoutes.data.sockets.entity.BaseResponse
import com.leodroidcoder.stockqoutes.di.qualifier.AppContext
import com.neovisionaries.ws.client.WebSocketFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(BaseResponse::class.java, ResponseDeserializer())
        .create()

    @Singleton
    @Provides
    fun provideWebSocketClient() = WebSocketFactory()
        .setConnectionTimeout(WebSocketsClient.CONNECTION_TIMEOUT)
        .createSocket(BuildConfig.WEBSOCKET_HOST)

    @Singleton
    @Provides
    fun provideWebSocketsManager(client: WebSocketsClient, gson: Gson) = WebSocketsManager(client, gson)

    @Provides
    @Singleton
    fun provideDataBase(@AppContext context: Context): AppDataBase {
        val builder = Room.databaseBuilder(context, AppDataBase::class.java, "stock_quotes")
        if (BuildConfig.DEBUG)
            builder.fallbackToDestructiveMigration()
        return builder.build()
    }
}