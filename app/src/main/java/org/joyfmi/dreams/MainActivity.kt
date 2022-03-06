package org.joyfmi.dreams

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("NoStart", "MainActivity onCreate 1")
        super.onCreate(savedInstanceState)
        Log.d("NoStart", "MainActivity onCreate 2")
        setContentView(R.layout.activity_main)
        Log.d("NoStart", "MainActivity onCreate 3")
    }
}