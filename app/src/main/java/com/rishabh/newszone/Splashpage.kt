package com.rishabh.newszone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

//@Suppress("DEPRECATION")
class Splashpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashpage)
        Handler().postDelayed({
            val startAct = Intent(this@Splashpage,
                MainActivity::class.java)
            startActivity(startAct)
        },2000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
