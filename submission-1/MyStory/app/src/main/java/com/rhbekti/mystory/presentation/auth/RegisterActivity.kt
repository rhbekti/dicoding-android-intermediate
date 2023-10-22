package com.rhbekti.mystory.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.rhbekti.mystory.data.model.Result
import com.rhbekti.mystory.databinding.ActivityRegisterBinding
import com.rhbekti.mystory.presentation.factory.AuthModelFactory
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel by viewModels<AuthViewModel> {
        AuthModelFactory.getInstance(this)
    }

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initialForm()
        setFormValues()
        setupAction()
    }

    private fun setupAction() {
        binding?.btnSubmit?.setOnClickListener {
            getFormValues()
            if (registerViewModel.validateRegister()) {
                processRegistration()
            }
        }
    }

    private fun getFormValues() {
        name = registerViewModel.name.toString()
        email = registerViewModel.email.toString()
        password = registerViewModel.password.toString()
    }

    private fun setFormValues() {
        binding?.tfName?.editText?.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setNameValue(
                text.toString()
            )
        }

        binding?.tfEmail?.editText?.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setEmailValue(text.toString())
        }

        binding?.tfPassword?.editText?.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setPasswordValue(text.toString())
        }
    }

    private fun initialForm() {
        binding?.apply {
            tfName.editText?.setText(registerViewModel.name)
            tfEmail.editText?.setText(registerViewModel.email)
            tfPassword.editText?.setText(registerViewModel.password)
        }
    }

    private fun processRegistration() {
        lifecycleScope.launch {
            registerViewModel.registerUser(name, email, password)
                .observe(this@RegisterActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(
                                    this@RegisterActivity, result.data?.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                registerViewModel.clearRegister()
                                initialForm()

                                val intent =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }

                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(
                                    this@RegisterActivity, result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}