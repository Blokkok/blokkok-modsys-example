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

            createFunction("MainActivity_addText") {
                for (item in it) {
                    if (item !is String) continue

                    binding.randomHolder.addView(
                        TextView(this@MainActivity).apply {
                            text = item
                        }
                    )
                }
            }
        }

        binding.callFunc.setOnClickListener {
            try {
                ModuleManager.executeCommunications {
                    invokeFunction("/example-module", "say-hello")
                    invokeFunction("/example-module", "say-something", listOf("Something"))
                }
            } catch (e: NotDefinedException) {
                Toast.makeText(
                    this,
                    "The module isn't loaded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.startStream.setOnClickListener {
            try {
                ModuleManager.executeCommunications {
                    openStream("/example-module", "example-stream") {
                        send("Hello world! ${this@MainActivity::class.java.name}")
                        send("This data is sent from the app, into the module and then gets" +
                                " bounced back into the app by calling the function addText all " +
                                "through a stream")
                    }
                }
            } catch (e: NotDefinedException) {
                Toast.makeText(
                    this,
                    "The module isn't loaded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        onStartBroadcaster.broadcast()
    }
}