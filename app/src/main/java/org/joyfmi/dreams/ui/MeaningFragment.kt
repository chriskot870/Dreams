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
        arguments?.let {
            symbolIdentity = it.get("symbolIdentity") as SymbolIdentity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MeaningFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
         * Build the list of Meanings
         */
        binding.textMeaningSymbol.text = symbolIdentity.name
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