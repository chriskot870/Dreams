package org.joyfmi.dreams

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.repository.Meaning
import org.joyfmi.dreams.databinding.MeaningItemBinding

class MeaningAdapter(): ListAdapter<Meaning, MeaningAdapter.MeaningViewHolder>(DiffCallback)  {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Meaning>() {
            override fun areItemsTheSame(oldItem: Meaning, newItem: Meaning): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Meaning, newItem: Meaning): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningAdapter.MeaningViewHolder {
        val viewHolder = MeaningAdapter.MeaningViewHolder(
            MeaningItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        Log.d("MeaningAdapter", "Creating View Holder")
        return viewHolder
    }

    override fun onBindViewHolder(holder: MeaningAdapter.MeaningViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeaningViewHolder(
        private var binding: MeaningItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(meaning: Meaning) {
            binding.referenceTextView.text = meaning.reference
            binding.contentTextView.text = meaning.contents
        }
    }
}