package org.joyfmi.dreams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.database.category.Category
import org.joyfmi.dreams.database.symbol.Symbol
import org.joyfmi.dreams.databinding.SymbolItemBinding
import org.joyfmi.dreams.databinding.CategoryItemBinding

class SymbolAdapter(
        private val onItemClicked: (Symbol) -> Unit
    ) : ListAdapter<Symbol, SymbolAdapter.SymbolViewHolder>(DiffCallback) {

        companion object {
            private val DiffCallback = object : DiffUtil.ItemCallback<Symbol>() {
                override fun areItemsTheSame(oldItem: Symbol, newItem: Symbol): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Symbol, newItem: Symbol): Boolean {
                    return oldItem == newItem
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolAdapter.SymbolViewHolder {
        val viewHolder = SymbolAdapter.SymbolViewHolder(
            SymbolItemBinding.inflate(
                LayoutInflater.from(parent.context),
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

    override fun onBindViewHolder(holder: SymbolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SymbolViewHolder(
        private var binding: SymbolItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(symbol: Symbol) {
            binding.symbolTextView.text = symbol.name
        }
    }
}