package com.rhbekti.mystory.presentation.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhbekti.mystory.R
import com.rhbekti.mystory.adapter.StoryAdapter
import com.rhbekti.mystory.data.local.entity.StoryEntity
import com.rhbekti.mystory.data.model.Result
import com.rhbekti.mystory.databinding.ActivityStoryBinding
import com.rhbekti.mystory.presentation.auth.LoginActivity
import com.rhbekti.mystory.presentation.factory.StoryModelFactory
import com.rhbekti.mystory.presentation.upload.UploadActivity
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {

    private val storyViewModel by viewModels<StoryViewModel> {
        StoryModelFactory.getInstance(this)
    }

    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        storyAdapter = StoryAdapter()

        lifecycleScope.launch {
            storyViewModel.getSession().collect { user ->
                if (!user.isLogin) {
                    startActivity(Intent(this@StoryActivity, LoginActivity::class.java))
                    finish()
                } else {
                    storyViewModel.getAllStories(user.token).observe(this@StoryActivity) { result ->
                        showResultStories(result)
                    }
                }
            }
        }

        binding?.rvStories?.apply {
            layoutManager = LinearLayoutManager(this@StoryActivity)
            setHasFixedSize(true)
        }

        binding?.btnUpload?.setOnClickListener {
            startActivity(Intent(this@StoryActivity, UploadActivity::class.java))
        }
    }

    private fun showResultStories(result: Result<List<StoryEntity>>?) {
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    val storiesData = result.data
                    storyAdapter.submitList(storiesData)
                    binding?.rvStories?.adapter = storyAdapter
                }

                is Result.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(
                        this, result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out -> {
                storyViewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}