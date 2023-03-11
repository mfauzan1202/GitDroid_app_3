package com.fauzan.submission3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fauzan.submission3.UserAdapter.MyViewHolder
import com.fauzan.submission3.data.local.entity.UserEntity
import com.fauzan.submission3.databinding.ItemUserBinding
import com.fauzan.submission3.ui.detail.DetailActivity

class UserAdapter(private val onLoveClick: (UserEntity) -> Unit, val context: Context?)
    : ListAdapter<UserEntity, MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(Response: UserEntity) {
            binding.apply {
                Glide.with(itemView)
                    .load(Response.avatar_url)
                    .into(imgItemPhoto)
                val newText =
                    context?.resources?.getString(R.string.userplaceholder, Response.login)
                tvItemName.text = newText
            }
        }

        fun onClickItem(Response: UserEntity) {
            val moveWithObjectIntent = Intent(context, DetailActivity::class.java)
            moveWithObjectIntent.putExtra(DetailActivity.EXTRA_USERNAME, Response.login)
            context?.startActivity(moveWithObjectIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivFavorite = holder.binding.ivLove
        if (user.isLoved) {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_baseline_favorite_24))
        } else {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_baseline_favorite_border_24))
        }

        ivFavorite.setOnClickListener {
            onLoveClick(user)
        }

        holder.itemView.setOnClickListener {
            holder.onClickItem(user)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.login == newUser.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}