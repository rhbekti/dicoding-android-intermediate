package com.rhbekti.storyapp.ui.auth.login

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
import com.rhbekti.storyapp.R
import com.rhbekti.storyapp.data.Result
import com.rhbekti.storyapp.data.prefs.UserModel
import com.rhbekti.storyapp.databinding.ActivityLoginBinding
import com.rhbekti.storyapp.ui.auth.AuthModelFactory
import com.rhbekti.storyapp.ui.auth.register.RegisterActivity
import com.rhbekti.storyapp.ui.main.story.StoryActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        AuthModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        sessionCheck()

        initialForm()
        setFormValues()
        playAnimation()

        binding.btnSubmit.setOnClickListener {
            actionLogin()
        }

        binding.btnRegister.setOnClickListener { toRegister() }
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val tfEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(300)
        val tfPassword =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(300)
        val btnSubmit = ObjectAnimator.ofFloat(binding.btnSubmit, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(tfEmail, tfPassword, btnSubmit)
            startDelay = 1000
        }.start()
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

    private fun sessionCheck() {
        lifecycleScope.launch {
            viewModel.getSession().collect { session ->
                if (session.isLogin) {
                    startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
                }
            }
        }
    }

    private fun toRegister() {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        finish()
    }

    private fun actionLogin() {
        lifecycleScope.launch {
            if (!viewModel.validate()) {
                viewModel.login().collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnSubmit.isEnabled = false
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSubmit.isEnabled = true

                            val user = result.data.loginResult

                            viewModel.saveSession(
                                UserModel(
                                    user?.userId.toString(),
                                    user?.name.toString(),
                                    user?.token.toString(),
                                    true
                                )
                            )

                            result.data.message?.let { showToast(it) }
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSubmit.isEnabled = true
                            showToast(result.error)
                        }
                    }
                }
            } else {
                showToast(getString(R.string.error_field_empty))
                binding.btnSubmit.isEnabled = true
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setFormValues() {
        binding.edtEmail.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.account.email = text.toString()
        }

        binding.edtPassword.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.account.password = text.toString()
        }
    }

    private fun initialForm() {
        viewModel.account.apply {
            binding.apply {
                edtEmail.editText?.setText(email)
                edtPassword.editText?.setText(password)
            }
        }
    }
}