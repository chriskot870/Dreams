package org.joyfmi.dreams.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.databinding.MeaningFragmentBinding
import org.joyfmi.dreams.repository.SymbolIdentity
import org.joyfmi.dreams.viewmodels.MeaningViewModel
import org.joyfmi.dreams.viewmodels.MeaningViewModelFactory

class MeaningFragment: Fragment() {

    private var _binding: MeaningFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: MeaningViewModel by activityViewModels {
        MeaningViewModelFactory(
            (activity?.application as DreamApplication).repository
        )
    }
    private lateinit var symbolIdentity: SymbolIdentity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MeaningFragment", "Creating")
        arguments?.let {
            symbolIdentity = it.get("symbolIdentity") as SymbolIdentity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MeaningFragment", "Creating View")
        _binding = MeaningFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MeaningFragment", "Created View")
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val meaningAdapter = MeaningListAdapter()
        recyclerView.adapter = meaningAdapter

        //lifecycle.coroutineScope.launch {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.getMeaningsBySymbolIdentity(symbolIdentity).collect() {
                meaningAdapter.submitList(it)
            }
        }
        viewModel.loadSymbolMeanings(symbolIdentity, meaningAdapter)
    }
}