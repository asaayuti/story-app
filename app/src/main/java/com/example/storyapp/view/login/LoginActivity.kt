package com.example.storyapp.view.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.data.Result
import com.example.storyapp.data.api.response.LoginResult
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            viewModel.saveSession(
                                LoginResult(
                                    result.data.loginResult?.name,
                                    result.data.loginResult?.userId,
                                    result.data.loginResult?.token
                                )
                            )
                            AlertDialog.Builder(this).apply {
                                setMessage("Login berhasil")
                                setPositiveButton("Lanjut") { _, _ ->
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                }
                                create()
                                show()
                            }
                            showLoading(false)
                        }

                        is Result.Error -> {
                            AlertDialog.Builder(this).apply {
                                setMessage("Password atau Email salah")
                                setNegativeButton("Coba Lagi") { dialog, _ -> dialog.dismiss() }
                                create()
                                show()
                            }
                            showLoading(false)
                        }
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}