package org.joyfmi.dreams.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joyfmi.dreams.databinding.CategoryFragmentBinding
import org.joyfmi.dreams.viewmodels.CategoryViewModel
import org.joyfmi.dreams.viewmodels.CategoryViewModelFactory
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.repository.CategoryIdentity

class CategoryFragment: Fragment() {

    private var _binding: CategoryFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    //private lateinit var categoryIdentity: CategoryIdentity

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
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val categoryAdapter = CategoryListAdapter {
            val action = CategoryFragmentDirections.actionCategoryFragmentToSymbolFragment(
                categoryIdentity = it
            )
            view.findNavController().navigate(action)
        }
        recyclerView.adapter = categoryAdapter
        /*
         * Go get the Categories and Submit it to the Adapter
         */
        viewModel.viewModelScope.launch(Dispatchers.IO) {
                viewModel.getAllCategories().collect() {
                    categoryAdapter.submitList(it)
                }

        }
        /*
        Log.d("UMD", "calling loadCategoryList")
        viewModel.loadCategoryList(categoryAdapter)
        Log.d("UMD", "came back from loadCategoryList")
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}