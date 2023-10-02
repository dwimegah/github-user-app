package com.belajar.submissionfundamental1.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.belajar.submissionfundamental1.data.response.DetailUserResponse
import com.belajar.submissionfundamental1.databinding.UserDetailBinding

class UserDetailAdapter : ListAdapter<DetailUserResponse, UserDetailAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
    class MyViewHolder(val binding: UserDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DetailUserResponse){
            binding.detailUsername.text = "${user.login}"
            binding.detailName.text = "${user.name}"
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailUserResponse>() {
            override fun areItemsTheSame(oldItem: DetailUserResponse, newItem: DetailUserResponse): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DetailUserResponse, newItem: DetailUserResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}