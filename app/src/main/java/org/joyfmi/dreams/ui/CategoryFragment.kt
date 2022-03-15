package org.joyfmi.dreams.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

/*
 * We define a Fragment for the Category page to be of class Fragment.
 * We also want CategoryFragment to implement the AdapterView.OnItemClickListener interface.
 * The comma after Fragment() indicates this. The OnItemClickListener is needed to notice
 * the Symbol AutoComplete. For the AdapterView.OnItemClickListener interface we need to override
 * it's onItemClick() function. By adding this function we can use this class as the
 * onItemClickListener for the SymbolNameView. When an item on the list is clicked this class'
 * onItemClick will get control and can use the navigation info to go to the SymbolFragment and
 * provide the symbolIdentity.
 */
class CategoryFragment: Fragment(), AdapterView.OnItemClickListener {

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
        /*
         * set the threshold to 1 so that single digit numbers will show up and can be selected
         */
        symbolNameView.threshold = 1
        symbolNameView.onItemClickListener = this
        symbolNameView.setAdapter(symbolNameAdapter)
        viewModel.loadSymbolList(symbolNameAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
     * This function is needed to implement the AdapterView.OnItemClickListener so that this
     * class can be used as the SymbolNameView's onItemClickListener.
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        /*
         * get the value clicked on from it's position value in the parent AdapterView
         */
        val identity = parent?.getItemAtPosition(pos)
        /*
         * Define the action from the Navigator control info that takes us to the Meaning Fragment.
         * Pass the identity as the symbolIdentity value to the Meaning Fragment
         */
        val action = CategoryFragmentDirections.actionCategoryFragmentToMeaningFragment(
            symbolIdentity = identity as SymbolIdentity
        )
        /*
         * The view that has the navigation info on how to get to the Meaning Fragment is in
         * the binding.root value. So, pass it the action to get to the Meaning Fragment
         */
        val categoryView = binding.root
        categoryView.findNavController().navigate(action)
    }

}