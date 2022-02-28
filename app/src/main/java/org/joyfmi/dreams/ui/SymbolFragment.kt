package org.joyfmi.dreams.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.joyfmi.dreams.databinding.SymbolFragmentBinding
import org.joyfmi.dreams.viewmodels.SymbolViewModel
import org.joyfmi.dreams.viewmodels.SymbolViewModelFactory
import kotlinx.coroutines.flow.collect
import org.joyfmi.dreams.DreamApplication

class SymbolFragment: Fragment() {

    private var _binding: SymbolFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: SymbolViewModel by activityViewModels {
        SymbolViewModelFactory(
            (activity?.application as DreamApplication).repository
        )
    }
    private var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SymbolFragment", "Creating")
        arguments?.let {
            categoryId = it.getInt("categoryId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SymbolFragment", "Creating View")
        _binding = SymbolFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SymbolFragment", "View Created")
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val symbolAdapter = SymbolAdapter({
            val action = SymbolFragmentDirections.actionSymbolFragmentToMeaningFragment(
                symbolId = it.id
            )
            view.findNavController().navigate(action)
        })
        recyclerView.adapter = symbolAdapter
        lifecycle.coroutineScope.launch {
            viewModel.symbolsByCategoryId(categoryId)?.collect() {
                symbolAdapter.submitList(it)
            }
        }
    }
}