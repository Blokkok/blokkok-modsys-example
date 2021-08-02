package com.blokkok.modsys.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blokkok.modsys.ModuleManager
import com.blokkok.modsys.example.databinding.ActivityMainBinding
import com.blokkok.modsys.modinter.exception.NotDefinedException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
    }
}