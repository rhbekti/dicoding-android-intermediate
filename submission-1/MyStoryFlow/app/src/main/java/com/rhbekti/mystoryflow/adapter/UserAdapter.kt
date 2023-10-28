package com.rhbekti.mystoryflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rhbekti.mystoryflow.data.model.Users
import com.rhbekti.mystoryflow.databinding.UsersItemBinding

class UserAdapter : ListAdapter<Users, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UsersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val users = getItem(position)
        holder.bind(users)
    }

    class UserViewHolder(private val binding: UsersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: Users?) {
            binding.apply {
                tvName.text = users?.name
                tvEmail.text = users?.email
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Users> =
            object : DiffUtil.ItemCallback<Users>() {
                override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
                    return oldItem == newItem
                }

            }
    }
}