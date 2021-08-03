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
            comContext.invokeFunction("MainActivity_addText",
                listOf("Hello World! I'm from ${javaClass.name}!")
            )
        }

        comContext.createFunction("say-something") { args ->
            comContext.invokeFunction("MainActivity_addText",
                listOf(args[0] as String)
            )
        }

        comContext.createStream("example-stream") {
            val data = ArrayList<String>()
            while (!closed) {
                data.add(recvBlock() as String)
            }

            comContext.invokeFunction("MainActivity_addText", data)
        }

        val subscription = comContext.subscribeToBroadcast("MainActivity_onStart") {
            Log.d(TAG, "onLoaded: I see MainActivity being started")

            comContext.invokeFunction("MainActivity_addText",
                listOf("onStart!")
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