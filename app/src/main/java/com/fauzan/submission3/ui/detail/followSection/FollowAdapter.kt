package com.fauzan.submission3.ui.detail.followSection

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fauzan.submission3.R
import com.fauzan.submission3.data.remote.response.ItemsItem
import com.fauzan.submission3.databinding.ItemFollowUserBinding
import com.fauzan.submission3.ui.detail.DetailActivity

class FollowAdapter(private val listUser: ArrayList<ItemsItem>, val context: Context?) :
    RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    fun setList(user: ArrayList<ItemsItem>) {
        listUser.clear()
        listUser.addAll(user)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemFollowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(Response: ItemsItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(Response.avatarUrl)
                    .into(imgItemPhoto)
                val newText =
                    context?.resources?.getString(R.string.userplaceholder, Response.login)
                tvItemName.text = newText
            }
        }

        fun onClickItem(Response: ItemsItem) {
            val moveWithObjectIntent = Intent(context, DetailActivity::class.java)
            moveWithObjectIntent.putExtra(DetailActivity.EXTRA_USERNAME, Response.login)
            context?.startActivity(moveWithObjectIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemFollowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])

        holder.itemView.setOnClickListener {
            holder.onClickItem(listUser[position])
        }
    }

    override fun getItemCount() = listUser.size
}