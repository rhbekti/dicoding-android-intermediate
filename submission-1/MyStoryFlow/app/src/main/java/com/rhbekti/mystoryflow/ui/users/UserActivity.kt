package com.rhbekti.mystoryflow.ui.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhbekti.mystoryflow.adapter.UserAdapter
import com.rhbekti.mystoryflow.data.Result
import com.rhbekti.mystoryflow.data.model.Users
import com.rhbekti.mystoryflow.databinding.ActivityUserBinding
import com.rhbekti.mystoryflow.ui.ViewModelFactory
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<UserViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecylerView()
        initUsers()
    }

    private fun initUsers() {
        lifecycleScope.launch {
            /* use flow
            viewModel.getUsers().collect{
                result ->
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        userAdapter.submitList(result.data)
                        binding.rvUsers.apply {
                            adapter = userAdapter
                        }
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@UserActivity,result.error,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            */

            // use live data
            viewModel.getUsers()
            viewModel.usersList.observe(this@UserActivity) { result ->
                setResult(result)
            }
        }
    }

    private fun setResult(result: Result<List<Users>>?) {
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    userAdapter.submitList(result.data)
                    binding.rvUsers.apply {
                        addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
                        adapter = userAdapter
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@UserActivity, result.error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun initRecylerView() {
        userAdapter = UserAdapter()
        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@UserActivity)
            adapter = userAdapter
        }
    }
}