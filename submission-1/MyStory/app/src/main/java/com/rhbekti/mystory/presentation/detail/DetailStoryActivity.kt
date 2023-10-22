package com.rhbekti.mystory.presentation.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rhbekti.mystory.data.model.Result
import com.rhbekti.mystory.data.remote.response.Story
import com.rhbekti.mystory.databinding.ActivityDetailStoryBinding
import com.rhbekti.mystory.presentation.auth.LoginActivity
import com.rhbekti.mystory.presentation.factory.StoryModelFactory
import com.rhbekti.mystory.presentation.story.StoryActivity
import kotlinx.coroutines.launch

class DetailStoryActivity : AppCompatActivity() {

    private val storyViewModel by viewModels<DetailStoryViewModel> {
        StoryModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra("id")

        lifecycleScope.launch {
            storyViewModel.getSession().collect { user ->
                if (!user.isLogin) {
                    startActivity(Intent(this@DetailStoryActivity, LoginActivity::class.java))
                    finish()
                } else {
                    storyViewModel.getDetailStory(user.token, id.toString())
                        .observe(this@DetailStoryActivity) { story ->
                            setStoryContent(story)
                        }
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DetailStoryActivity, StoryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun setStoryContent(story: Result<Story?>?) {
        if (story != null) {
            when (story) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    Glide.with(this@DetailStoryActivity).load(story.data?.photoUrl)
                        .transform(CenterCrop(), RoundedCorners(30))
                        .into(binding.ivStory)

                    binding.apply {
                        tvName.text = story.data?.name
                        tvDescription.text = story.data?.description
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this, story.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}