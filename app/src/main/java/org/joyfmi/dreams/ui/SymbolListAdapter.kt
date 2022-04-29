package org.joyfmi.dreams.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.databinding.SymbolItemBinding
import org.joyfmi.dreams.repository.DB_LOCAL
import org.joyfmi.dreams.repository.SymbolIdentity

class SymbolListAdapter(
        private val onItemClicked: (SymbolIdentity) -> Unit
    ) : ListAdapter<SymbolIdentity, SymbolListAdapter.SymbolViewHolder>(DiffCallback) {

        companion object {
            private val DiffCallback = object : DiffUtil.ItemCallback<SymbolIdentity>() {
                override fun areItemsTheSame(oldItem: SymbolIdentity, newItem: SymbolIdentity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: SymbolIdentity, newItem: SymbolIdentity): Boolean {
                    return oldItem == newItem
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder {
        Log.d("SymbolAdapter", "Creating")
        val viewHolder = SymbolViewHolder(
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
        fun bind(symbolIdentity: SymbolIdentity) {
            Log.d("SymbolViewHolder", "Assigning Values")
            binding.symbolTextView.text = symbolIdentity.name
            if ( symbolIdentity.local == DB_LOCAL ) {
                binding.symbolTextView.setTextColor(DreamApplication.localTextColor)
            } else {
                binding.symbolTextView.setTextColor(DreamApplication.commonTextColor)
            }
        }
    }
}