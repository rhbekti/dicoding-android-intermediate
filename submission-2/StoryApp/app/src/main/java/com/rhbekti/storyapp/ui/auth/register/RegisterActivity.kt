package com.rhbekti.storyapp.ui.auth.register

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
import com.rhbekti.storyapp.databinding.ActivityRegisterBinding
import com.rhbekti.storyapp.ui.auth.AuthModelFactory
import com.rhbekti.storyapp.ui.auth.login.LoginActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        AuthModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        initialForm()
        setFormValues()
        playAnimation()

        binding.btnSubmit.setOnClickListener { actionRegister() }
        binding.btnLogin.setOnClickListener { toLogin() }
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val tfName = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(300)
        val tfEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(300)
        val tfPassword =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(300)
        val btnSubmit = ObjectAnimator.ofFloat(binding.btnSubmit, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(tfName, tfEmail, tfPassword, btnSubmit)
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

    private fun toLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    private fun initialForm() {
        viewModel.account.apply {
            binding.apply {
                edtName.editText?.setText(name)
                edtEmail.editText?.setText(email)
                edtPassword.editText?.setText(password)
            }
        }
    }

    private fun setFormValues() {
        binding.edtName.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.account.name = text.toString()
        }

        binding.edtEmail.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.account.email = text.toString()
        }

        binding.edtPassword.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.account.password = text.toString()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun actionRegister() {
        lifecycleScope.launch {
            if (!viewModel.validate()) {
                viewModel.register().collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnSubmit.isEnabled = false
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnSubmit.isEnabled = true

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
}