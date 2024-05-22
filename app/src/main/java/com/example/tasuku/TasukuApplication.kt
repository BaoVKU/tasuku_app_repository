package com.example.tasuku

import android.app.Application
import com.example.tasuku.data.AppContainer
import com.example.tasuku.data.DefaultAppContainer
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

class TasukuApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        val statePluginFactory =
            StreamStatePluginFactory(config = StatePluginConfig(), appContext = this)
        ChatClient.Builder(getString(R.string.api_key), applicationContext)
            .withPlugins(statePluginFactory).logLevel(ChatLogLevel.ALL).build()
        container = DefaultAppContainer(
            this.getSharedPreferences(
                getString(R.string.app_name),
                MODE_PRIVATE
            )
        )
    }
}