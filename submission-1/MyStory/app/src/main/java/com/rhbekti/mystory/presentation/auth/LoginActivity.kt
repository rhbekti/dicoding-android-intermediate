package com.rhbekti.mystory.presentation.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.rhbekti.mystory.data.model.Result
import com.rhbekti.mystory.databinding.ActivityLoginBinding
import com.rhbekti.mystory.presentation.factory.AuthModelFactory
import com.rhbekti.mystory.presentation.story.StoryActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<AuthViewModel> {
        AuthModelFactory.getInstance(this)
    }

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        lifecycleScope.launch {
            loginViewModel.getSession().collect { user ->
                if (user.isLogin) {
                    loginViewModel.clearLogin()
                    initialForm()
                    startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
                    finish()
                }
            }
        }

        setupView()
        initialForm()
        setFormValues()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {

        val tfEmail = ObjectAnimator.ofFloat(binding?.tfEmail, View.ALPHA, 1f).setDuration(300)
        val tfPassword =
            ObjectAnimator.ofFloat(binding?.tfPassword, View.ALPHA, 1f).setDuration(300)
        val btnSubmit = ObjectAnimator.ofFloat(binding?.btnSubmit, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(tfEmail, tfPassword, btnSubmit)
            startDelay = 1000
        }.start()
    }

    private fun setupAction() {
        binding?.btnSubmit?.setOnClickListener {
            getFormValues()
            if (loginViewModel.validateLogin()) {
                processLogin()
            }
        }

        binding?.btnRegister?.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun processLogin() {
        lifecycleScope.launch {
            loginViewModel.loginUser(email, password).observe(this@LoginActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity, result.data?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity, result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun getFormValues() {
        email = loginViewModel.email.toString()
        password = loginViewModel.password.toString()
    }

    private fun setFormValues() {
        binding?.tfEmail?.editText?.doOnTextChanged { text, _, _, _ ->
            loginViewModel.setEmailValue(text.toString())
        }

        binding?.tfPassword?.editText?.doOnTextChanged { text, _, _, _ ->
            loginViewModel.setPasswordValue(text.toString())
        }
    }

    private fun initialForm() {
        binding?.apply {
            tfEmail.editText?.setText(loginViewModel.email)
            tfPassword.editText?.setText(loginViewModel.password)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}