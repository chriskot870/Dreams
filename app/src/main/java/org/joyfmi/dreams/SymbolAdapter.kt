package org.joyfmi.dreams

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.database.symbol.Symbol
import org.joyfmi.dreams.databinding.SymbolItemBinding

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
        Log.d("SymbolAdapter", "Creating")
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
        Log.d("SymbolFragment", "Bind ViewHolder")
        holder.bind(getItem(position))
    }

    class SymbolViewHolder(
        private var binding: SymbolItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(symbol: Symbol) {
            Log.d("SymbolViewHolder", "Assigning Values")
            binding.symbolTextView.text = symbol.name
        }
    }
}