package com.iyxan.module

import android.util.Log
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class ExampleModule : Module() {

    override val namespace: String get() = "example-module"

    @Suppress("PrivatePropertyName")
    private val TAG = "ExampleModule"

    override fun onLoaded(comContext: CommunicationContext) {
        Log.d(TAG, "onLoad: Hello world! I have been loaded!")

        comContext.createFunction("say-hello") {
            Log.d(TAG, "say-hello: Hello world! I'm from ${javaClass.name}")
        }

        comContext.createFunction("say-something") { args ->
            Log.d(TAG, "say-something: ${args[0]}")
        }

        val subscription = comContext.subscribeToBroadcast("MainActivity_onStart") {
            Log.d(TAG, "onLoaded: I see MainActivity being started")

            comContext.invokeFunction("MainActivity_addText",
                listOf(
                    "Hello",
                    "World!",
                    "I'm from ${javaClass.name}!"
                )
            )
        }

        comContext.createFunction("unsubscribe") {
            subscription.unsubscribe()
        }
    }

    override fun onUnloaded(comContext: CommunicationContext) {
        Log.d(TAG, "onExit: Bye world! I'm being unloaded!")
    }
}