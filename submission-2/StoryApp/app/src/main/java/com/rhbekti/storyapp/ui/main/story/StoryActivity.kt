package com.rhbekti.storyapp.ui.main.story

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhbekti.storyapp.R
import com.rhbekti.storyapp.adapter.LoadingStateAdapter
import com.rhbekti.storyapp.adapter.StoryListAdapter
import com.rhbekti.storyapp.databinding.ActivityStoryBinding
import com.rhbekti.storyapp.ui.auth.login.LoginActivity
import com.rhbekti.storyapp.ui.main.StoryModelFactory
import com.rhbekti.storyapp.ui.main.maps.MapsActivity
import com.rhbekti.storyapp.ui.main.uploads.AddStoryActivity
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        StoryModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkSession()
        initRecyler()
        getData()

        binding.btnAddStory.setOnClickListener {
            toAddStory()
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun toAddStory() {
        startActivity(Intent(this@StoryActivity, AddStoryActivity::class.java))
    }

    private fun initRecyler() {
        storyAdapter = StoryListAdapter()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@StoryActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }

    private fun getData() {

        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        viewModel.listStory.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    private fun checkSession() {
        lifecycleScope.launch {
            viewModel.getSession().collect { session ->
                if (!session.isLogin) {
                    startActivity(Intent(this@StoryActivity, LoginActivity::class.java))
                    finish()
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
            R.id.maps -> {
                startActivity(Intent(this@StoryActivity, MapsActivity::class.java))
            }

            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.logout -> {
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}