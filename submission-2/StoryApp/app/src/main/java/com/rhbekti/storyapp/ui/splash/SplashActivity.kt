package com.rhbekti.storyapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.rhbekti.storyapp.R
import com.rhbekti.storyapp.ui.auth.AuthModelFactory
import com.rhbekti.storyapp.ui.auth.login.LoginActivity
import com.rhbekti.storyapp.ui.main.story.StoryActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        AuthModelFactory.getInstance(this)
    }

    private val duration: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(duration)

            viewModel.getSession().collect { session ->
                if (session.isLogin) {
                    startActivity(Intent(this@SplashActivity, StoryActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}