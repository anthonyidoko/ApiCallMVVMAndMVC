package com.example.mvvmarchitecture.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.ui.mvc.MVCActivity
import com.example.mvvmarchitecture.ui.mvvm.MVVMActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mvvmbtn :Button = findViewById(R.id.mvvm)
        val btnMVC :Button = findViewById(R.id.mvc)

        mvvmbtn.setOnClickListener {
            val intent = Intent(this, MVVMActivity::class.java)
            startActivity(intent)
        }

        btnMVC.setOnClickListener {
            val intent = Intent(this, MVCActivity::class.java)
            startActivity(intent)
        }



    }
}