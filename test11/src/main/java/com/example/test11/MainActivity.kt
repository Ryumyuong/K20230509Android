package com.example.test11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.test11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /*override fun onSupportNavigateUp(): Boolean {
        Toast.makeText(this@MainActivity, "메인 업버튼 동작",Toast.LENGTH_SHORT).show()
        onBackPressed()
        return super.onSupportNavigateUp()
    }*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuItem1 : MenuItem? = menu?.add(0,0,0,"메뉴1")
        val menuItem2 : MenuItem? = menu?.add(0,1,0,"메뉴2")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        0 -> {
            Toast.makeText(this@MainActivity, "메뉴 1", Toast.LENGTH_SHORT).show()
            true
        }

        1 -> {
            val intent = Intent(this@MainActivity, ThreeActivity::class.java)
            startActivity(intent)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //화면 전환
        binding.btn1.setOnClickListener {
            val intent = Intent(this@MainActivity, TwoActivity::class.java)
            startActivity(intent)
        }

    }
}