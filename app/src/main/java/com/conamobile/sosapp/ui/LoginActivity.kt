package com.conamobile.sosapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.conamobile.sosapp.databinding.ActivityLoginBinding
import com.conamobile.sosapp.pref.SharedPreferences

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val sharedPreferences by lazy {
        SharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            if (sharedPreferences.isLogin()) {
                name.setText(sharedPreferences.isName())
                phone.setText(sharedPreferences.isPhone())
                helpNumber.setText(sharedPreferences.isHelpNumber())
                date.setText(sharedPreferences.isDate())
            }
            next.setOnClickListener {
                if (name.text.toString().isNotEmpty() &&
                    phone.text.toString().isNotEmpty() &&
                    helpNumber.text.toString().isNotEmpty() &&
                    date.text.toString().isNotEmpty()
                ) {
                    sharedPreferences.isName(name.text.toString())
                    sharedPreferences.isPhone(phone.text.toString())
                    sharedPreferences.isHelpNumber(helpNumber.text.toString())
                    sharedPreferences.isDate(date.text.toString())
                    sharedPreferences.isLogin(true)
                    finish()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Hammasini to'ldiring",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}