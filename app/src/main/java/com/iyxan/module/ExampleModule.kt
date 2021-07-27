package com.iyxan.module

import android.util.Log
import com.blokkok.modsys.ModuleLoader
import com.com.blokkok.modsys.modinter.Module

class ExampleModule(
    private val bridge: ModuleLoader.ModuleBridge
) : Module(bridge) {

    @Suppress("PrivatePropertyName")
    private val TAG = "ExampleModule"

    override fun onLoad() {
        Log.d(TAG, "onLoad: Hello world! I have been loaded!")

        bridge.subscribeToBroadcast("main_activity_on_launched") {
            Log.d(TAG, "I see MainActivity being launched!")

            bridge.invokeFunction(
                "add_text",
                "Hi",
                "world,",
                "I'm",
                "from",
                javaClass.name,
                "If you're seeing these, the bridge works!!"
            )
        }
    }

    override fun onExit() {
        Log.d(TAG, "onExit: Bye world! I have been unloaded!")
    }
}