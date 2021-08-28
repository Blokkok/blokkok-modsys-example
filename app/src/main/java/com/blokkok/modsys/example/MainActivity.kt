package com.blokkok.modsys.example

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blokkok.modsys.ModuleManager
import com.blokkok.modsys.communication.objects.Broadcaster
import com.blokkok.modsys.example.databinding.ActivityMainBinding
import com.blokkok.modsys.modinter.exception.NotDefinedException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var onStartBroadcaster: Broadcaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ModuleManager.initialize(this)

        binding.openManager.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, ModuleManagerActivity::class.java)
            )
        }

        ModuleManager.executeCommunications {
            onStartBroadcaster = createBroadcaster("MainActivity_onStart")
            claimFlag("example-flag")

            createFunction("MainActivity_addText") {
                for (item in it["texts"] as List<*>) {
                    if (item !is String) continue

                    runOnUiThread {
                        binding.randomHolder.addView(
                            TextView(this@MainActivity).apply {
                                text = item
                            }
                        )
                    }
                }
            }
        }

        binding.callFunc.setOnClickListener {
            try {
                ModuleManager.executeCommunications {
                    invokeFunction("/example-module", "say-hello")
                    invokeFunction("/example-module", "say-something", mapOf("texts" to "Hi World"))

                    invokeFunction("/example-module", "ann_test")
                    invokeFunction("/example-module", "ann_test2", mapOf("text" to "Hi World!"))
                    invokeFunction(
                        "/example-module",
                        "ann_test_optional",
                        mapOf("text" to "Hello")
                    )

                    invokeFunction(
                        "/example-module",
                        "ann_test_optional",
                        mapOf("text" to "Hello, I have a number", "number" to 100)
                    )

                    // namespaces
                    invokeFunction(
                        "/example-module/hello",
                        "test"
                    )

                    invokeFunction(
                        "/example-module/hello",
                        "test2",
                        mapOf("text" to "hello from the host")
                    )

                    invokeFunction(
                        "/example-module/hello/nested",
                        "something",
                    )
                }
            } catch (e: NotDefinedException) {
                Toast.makeText(
                    this,
                    "The module isn't loaded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.printNamespaces.setOnClickListener {
            ModuleManager.executeCommunications {
                val namespaces = getFlagNamespaces("example-flag")
                invokeFunction("MainActivity_addText", mapOf("texts" to listOf(namespaces)))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        onStartBroadcaster.broadcast()
    }
}