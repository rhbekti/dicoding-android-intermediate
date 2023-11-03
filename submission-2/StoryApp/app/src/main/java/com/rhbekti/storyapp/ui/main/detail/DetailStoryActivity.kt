package com.rhbekti.storyapp.ui.main.detail

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
import com.rhbekti.storyapp.databinding.ActivityDetailStoryBinding
import com.rhbekti.storyapp.ui.main.StoryModelFactory
import kotlinx.coroutines.launch
import com.rhbekti.storyapp.data.Result
import com.rhbekti.storyapp.ui.main.story.StoryActivity

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModels<DetailStoryViewModel> {
        StoryModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDetail()

        backToStory()
    }

    private fun backToStory() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DetailStoryActivity, StoryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun showDetail() {
        val id = intent.getStringExtra("id")
        if (id != null) {
            lifecycleScope.launch {
                viewModel.getDetailStory(id).collect { result ->
                    when (result) {
                        is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is Result.Success -> {
                            binding.apply {
                                progressBar.visibility = View.GONE
                                Glide.with(this@DetailStoryActivity)
                                    .load(result.data.story?.photoUrl)
                                    .transform(CenterCrop(), RoundedCorners(30)).into(ivStory)

                                tvName.text = result.data.story?.name
                                tvDescription.text = result.data.story?.description
                            }
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            showToast(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DetailStoryActivity, message, Toast.LENGTH_SHORT).show()
    }
}