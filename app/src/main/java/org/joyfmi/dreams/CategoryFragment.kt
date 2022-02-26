package org.joyfmi.dreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.databinding.CategoryFragmentBinding
import org.joyfmi.dreams.viewmodels.CategoryViewModel
import org.joyfmi.dreams.viewmodels.CategoryViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CategoryFragment: Fragment() {

    private var _binding: CategoryFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: CategoryViewModel by activityViewModels {
        CategoryViewModelFactory(
            (activity?.application as DreamApplication).database.categoryDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val categoryAdapter = CategoryListAdapter({
            val action = CategoryFragmentDirections.actionCategoryFragmentToSymbolFragment(
                categoryId = it.id
            )
            view.findNavController().navigate(action)
        })
        recyclerView.adapter = categoryAdapter
        lifecycle.coroutineScope.launch {
            viewModel.allCategories().collect() {
                categoryAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}