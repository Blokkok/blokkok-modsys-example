package com.iyxan.module

import android.util.Log
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module
import com.blokkok.modsys.modinter.annotations.Function
import com.blokkok.modsys.modinter.annotations.Namespace

@Suppress("unused")
class ExampleModule : Module() {

    override val namespace: String get() = "example-module"
    override val flags: List<String> = listOf("example-flag")

    @Suppress("PrivatePropertyName")
    private val TAG = "ExampleModule"

    override fun onLoaded(comContext: CommunicationContext) {
        Log.d(TAG, "onLoad: Hello world! I have been loaded!")

        comContext.createFunction("say-hello") {
            comContext.invokeFunction("MainActivity_addText",
                mapOf("texts" to listOf("Hello World! I'm from ${javaClass.name}!"))
            )
        }

        comContext.createFunction("say-something") { args ->
            comContext.invokeFunction("MainActivity_addText",
                mapOf("texts" to listOf(args["text"]))
            )
        }

        val subscription = comContext.subscribeToBroadcast("MainActivity_onStart") {
            Log.d(TAG, "onLoaded: I see MainActivity being started")

            comContext.invokeFunction("MainActivity_addText",
                mapOf("texts" to listOf("onStart!"))
            )
        }

        comContext.createFunction("unsubscribe") {
            subscription.unsubscribe()
        }
    }

    @Function(name = "ann_test")
    fun annotationTest() {
        Log.d(TAG, "annotationTest: Hello world!!")
    }

    @Function(name = "ann_test2")
    fun annotationTest2(text: String) {
        Log.d(TAG, "annotationTest: $text")
    }

    @Function(name = "ann_test_optional")
    fun annOptionalParamTest(text: String, number: Int = 5) {
        Log.d(TAG, "called with: text = $text, number = $number")
    }

    @Namespace(name = "hello")
    object HelloNamespace {
        @Function(name = "test")
        fun testFunction() {
            Log.d("HelloNamespace", "testFunction: hello world!")
        }

        @Function(name = "test2")
        fun testFunction2(text: String) {
            Log.d("HelloNamespace", "testFunction: hello world! arg: $text")
        }

        @Namespace(name = "nested")
        object NestedNamespace {
            @Function
            fun something() {
                Log.d("NestedNamespace", "something: it works!")
            }
        }
    }

    override fun onUnloaded(comContext: CommunicationContext) {
        Log.d(TAG, "onExit: Bye world! I'm being unloaded!")
    }
}