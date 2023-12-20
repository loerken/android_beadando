package com.horvathkaroly.feedthecat

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun startGame(@Suppress("UNUSED_PARAMETER")view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

