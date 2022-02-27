package org.joyfmi.dreams

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.joyfmi.dreams.databinding.MeaningFragmentBinding
import org.joyfmi.dreams.viewmodels.MeaningViewModel
import org.joyfmi.dreams.viewmodels.MeaningViewModelFactory

class MeaningFragment: Fragment() {

    private var _binding: MeaningFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: MeaningViewModel by activityViewModels {
        MeaningViewModelFactory(
            (activity?.application as DreamApplication).database.meaningDao()
        )
    }
    private var symbolId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MeaningFragment", "Creating")
        arguments?.let {
            symbolId = it.getInt("symbolId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val meaningAdapter = MeaningAdapter()
        recyclerView.adapter = meaningAdapter
        lifecycle.coroutineScope.launch {
            viewModel.getMeaningBySymbolId(symbolId).collect() {
                meaningAdapter.submitList(it)
            }
        }
    }
}