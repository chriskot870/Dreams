package org.joyfmi.dreams

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("NoStart", "MainActivity onCreate 1")
        super.onCreate(savedInstanceState)
        Log.d("NoStart", "MainActivity onCreate 2")
        setContentView(R.layout.activity_main)
        Log.d("NoStart", "MainActivity onCreate 3")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.new_symbol_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_symbol_menu_item -> {
                Toast.makeText(applicationContext, "click on New Symbol", Toast.LENGTH_LONG).show()
                true
            }
            R.id.settings ->{
                Toast.makeText(applicationContext, "click on Setting", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}