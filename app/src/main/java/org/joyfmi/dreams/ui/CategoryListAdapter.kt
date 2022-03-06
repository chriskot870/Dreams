package org.joyfmi.dreams.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.repository.CategoryIdentity
import org.joyfmi.dreams.databinding.CategoryItemBinding

class CategoryListAdapter(
    private val onItemClicked: (CategoryIdentity) -> Unit
) : ListAdapter<CategoryIdentity, CategoryListAdapter.CategoryViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CategoryIdentity>() {
            override fun areItemsTheSame(oldItem: CategoryIdentity, newItem: CategoryIdentity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CategoryIdentity, newItem: CategoryIdentity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val viewHolder = CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(
        private var binding: CategoryItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryIdentity: CategoryIdentity) {
            binding.categoryTextView.text = categoryIdentity.name
        }
    }
}