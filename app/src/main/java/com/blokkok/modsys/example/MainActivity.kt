package com.blokkok.modsys.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blokkok.modsys.ModuleManager
import com.blokkok.modsys.example.databinding.ActivityMainBinding

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

        ModuleManager.registerCommunications {
            // TODO: This
        }
    }
}