package com.iyxan.module

import android.util.Log
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module
import com.blokkok.modsys.modinter.annotations.ExtensionPoint
import com.blokkok.modsys.modinter.annotations.Function
import com.blokkok.modsys.modinter.annotations.ImplementsExtensionPoint
import com.blokkok.modsys.modinter.annotations.Namespace

@Suppress("unused")
class ExampleModule : Module() {

    override val namespace: String get() = "example-module2"
    override val flags: List<String> = listOf("example-flag")

    companion object {
        private const val TAG = "ExampleModule2"
    }

    override fun onLoaded(comContext: CommunicationContext) {
        Log.d(TAG, "onLoaded: Hi world, Example Module 2 is being loaded!")
    }

    override fun onUnloaded(comContext: CommunicationContext) {
        Log.d(TAG, "onExit: Bye world! I'm being unloaded!")
    }

    @ImplementsExtensionPoint(
        extPointNamespace = "/example-module",
        extPointName = "writer"
    )
    object WriterImpl {
        fun name(): String = "example-module2"

        fun print(str: String) {
            Log.d(TAG, "print: wrote \"$str\"!")
        }
    }
}