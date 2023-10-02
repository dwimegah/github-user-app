package com.belajar.submissionfundamental1.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.belajar.submissionfundamental1.data.database.FavoriteUser
import com.belajar.submissionfundamental1.databinding.UserItemBinding
import com.belajar.submissionfundamental1.ui.detail.UserDetail
import com.bumptech.glide.Glide

class FavoriteUserAdapter :
    ListAdapter<FavoriteUser, FavoriteUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class MyViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: FavoriteUser) {
            binding.username.text = itemName.username
            Glide.with(binding.root)
                .load(itemName.avatarUrl)
                .into(binding.avatar)
            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, UserDetail::class.java)
                intentDetail.putExtra("username", itemName.username)
                binding.root.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}