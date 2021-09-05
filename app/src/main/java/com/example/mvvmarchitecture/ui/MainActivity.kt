package com.example.mvvmarchitecture.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mvvmarchitecture.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mvvmbtn :Button = findViewById(R.id.mvvm)
        val btnMVC :Button = findViewById(R.id.mvc)

        mvvmbtn.setOnClickListener {
            val intent = Intent(this, PostsActivity::class.java)
            startActivity(intent)
        }

        btnMVC.setOnClickListener {
            val intent = Intent(this, MVCActivity::class.java)
            startActivity(intent)
        }



    }
}