package org.joyfmi.dreams.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.databinding.CategoryFragmentBinding
import org.joyfmi.dreams.viewmodels.CategoryViewModel
import org.joyfmi.dreams.viewmodels.CategoryViewModelFactory
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.R
import org.joyfmi.dreams.repository.SymbolIdentity

class CategoryFragment: Fragment() {

    private var _binding: CategoryFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var symbolNameView: AutoCompleteTextView

    private val viewModel: CategoryViewModel by activityViewModels {
        CategoryViewModelFactory(
            (activity?.application as DreamApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("NoStart", "CategoryFragment: onCreateView")
        _binding = CategoryFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
         * Setup the recyclerView in case they click on a category
         */
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val categoryAdapter = CategoryListAdapter {
            val action = CategoryFragmentDirections.actionCategoryFragmentToSymbolFragment(
                categoryIdentity = it
            )
            view.findNavController().navigate(action)
        }
        recyclerView.adapter = categoryAdapter
        viewModel.loadCategoryList(categoryAdapter)
        /*
         * Setup AutoCompletion in case they enter a Symbol
         */
        symbolNameView = binding.symbolName
        val symbolNameAdapter = ArrayAdapter<SymbolIdentity>(
            //(activity?.application as DreamApplication).applicationContext,
            requireContext(),
            R.layout.symbol_name_item,
            R.id.symbol_name_view)
            //mutableListOf<SymbolIdentity>())

        symbolNameView.setAdapter(symbolNameAdapter)
        viewModel.loadSymbolList(symbolNameAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}