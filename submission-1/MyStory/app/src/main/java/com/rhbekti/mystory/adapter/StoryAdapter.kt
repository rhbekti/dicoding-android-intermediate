package com.rhbekti.mystory.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rhbekti.mystory.data.local.entity.StoryEntity
import com.rhbekti.mystory.databinding.StoryItemsBinding
import com.rhbekti.mystory.presentation.detail.DetailStoryActivity
import androidx.core.util.Pair

class StoryAdapter : ListAdapter<StoryEntity, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
    }

    class StoryViewHolder(private val binding: StoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: StoryEntity?) {
            binding.apply {
                Glide.with(itemView.context).load(stories?.photoUrl).transform(
                    CenterCrop(),
                    RoundedCorners(30)
                ).into(ivStory)
                tvName.text = stories?.name
            }

            itemView.setOnClickListener {
                val optionsCompact: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity,Pair(binding.ivStory,"story_image"),Pair(binding.tvName,"story_name"))

                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("id", stories?.id)
                itemView.context.startActivity(intent,optionsCompact.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoryEntity> =
            object : DiffUtil.ItemCallback<StoryEntity>() {
                override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: StoryEntity,
                    newItem: StoryEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}