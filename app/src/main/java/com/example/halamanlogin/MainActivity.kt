package com.example.halamanlogin

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.example.halamanlogin.R.layout.welcomepage)


        val buttonlogin: Button = findViewById(R.id.button1)
        val buttonsignup: MaterialButton = findViewById(R.id.button2)


        buttonlogin.setOnClickListener {

            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }


        buttonsignup.setOnClickListener {

            val signUpIntent = Intent(this, Signup::class.java)
            startActivity(signUpIntent)
        }
    }
}