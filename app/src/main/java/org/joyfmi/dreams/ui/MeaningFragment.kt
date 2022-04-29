package org.joyfmi.dreams.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.databinding.MeaningFragmentBinding
import org.joyfmi.dreams.repository.DB_LOCAL
import org.joyfmi.dreams.repository.Symbol
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
        /*
         * Build the list of Meanings
         */
        binding.textMeaningSymbol.text = String.format("Symbol: %s", symbolIdentity.name)
        if ( symbolIdentity.local == DB_LOCAL ) {
            binding.textMeaningSymbol.setTextColor(DreamApplication.localTextColor)
        } else {
            binding.textMeaningSymbol.setTextColor(DreamApplication.commonTextColor)
        }
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val meaningAdapter = MeaningListAdapter()
        recyclerView.adapter = meaningAdapter
        viewModel.loadSymbolMeanings(symbolIdentity, meaningAdapter)
    }
}