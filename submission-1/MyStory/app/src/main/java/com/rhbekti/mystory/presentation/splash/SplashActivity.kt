package com.rhbekti.mystory.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.rhbekti.mystory.R
import com.rhbekti.mystory.presentation.factory.AuthModelFactory
import com.rhbekti.mystory.presentation.auth.AuthViewModel
import com.rhbekti.mystory.presentation.auth.LoginActivity
import com.rhbekti.mystory.presentation.story.StoryActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val authViewModel by viewModels<AuthViewModel> {
        AuthModelFactory.getInstance(this)
    }
    private val duration: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(duration)

            authViewModel.getSession().collect { user ->
                if (user.isLogin) {
                    val intent = Intent(this@SplashActivity, StoryActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}