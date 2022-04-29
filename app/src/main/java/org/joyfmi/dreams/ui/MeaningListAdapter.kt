package org.joyfmi.dreams.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.repository.MeaningIdentity
import org.joyfmi.dreams.databinding.MeaningItemBinding
import org.joyfmi.dreams.repository.DB_LOCAL
import org.joyfmi.dreams.repository.Meaning

class MeaningListAdapter(): ListAdapter<Meaning, MeaningListAdapter.MeaningViewHolder>(DiffCallback)  {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Meaning>() {
            override fun areItemsTheSame(oldItem: Meaning, newItem: Meaning): Boolean {
                return oldItem.identity.id == newItem.identity.id
            }

            override fun areContentsTheSame(oldItem: Meaning, newItem: Meaning): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningViewHolder {
        val viewHolder = MeaningViewHolder(
            MeaningItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        Log.d("MeaningAdapter", "Creating View Holder")
        return viewHolder
    }

    override fun onBindViewHolder(holder: MeaningViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MeaningViewHolder(
        private var binding: MeaningItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(meaning: Meaning) {
            /*
             * Local identities can be edited, but common ones can not be edited.
             */
            if ( meaning.identity.local == DB_LOCAL) {
                binding.meaningEditButton.visibility = View.VISIBLE
            } else {
                binding.meaningEditButton.visibility = View.GONE
            }
            binding.referenceTextView.text = meaning.reference
            binding.contentTextView.text = meaning.contents
        }
    }
}