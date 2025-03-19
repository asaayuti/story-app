package com.example.storyapp.view.login

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
        showLoading(false)
        binding.loginButton.setOnClickListener {
            // Check network connectivity before attempting login.
            if (!isNetworkAvailable()) {
                AlertDialog.Builder(this).apply {
                    setMessage("Network offline. Please check your internet connection.")
                    setNegativeButton("OK") { dialog, _ -> dialog.dismiss() }
                    create()
                    show()
                }
                return@setOnClickListener
            }

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
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    startActivity(intent)
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
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Helper function to check network availability
    @SuppressLint("ObsoleteSdkInt")
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            @Suppress("DEPRECATION")
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}
