package org.joyfmi.dreams.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.R
import org.joyfmi.dreams.databinding.AddSymbolFragmentBinding
import org.joyfmi.dreams.databinding.CategoryFragmentBinding
import org.joyfmi.dreams.repository.CategoryIdentity
import org.joyfmi.dreams.viewmodels.AddSymbolViewModel
import org.joyfmi.dreams.viewmodels.AddSymbolViewModelFactory
import org.joyfmi.dreams.viewmodels.CategoryViewModel
import org.joyfmi.dreams.viewmodels.CategoryViewModelFactory

class AddSymbolFragment : Fragment() {

    private var _binding: AddSymbolFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var submitButton: Button

    private lateinit var symbolName: EditText

    private lateinit var categoryName: EditText

    private lateinit var categorySpinner: Spinner

    private lateinit var reference: EditText

    private lateinit var meaning: EditText

    private val viewModel: AddSymbolViewModel by activityViewModels {
        AddSymbolViewModelFactory(
            (activity?.application as DreamApplication).repository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddSymbolFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        symbolName = binding.symbolInput
        categoryName = binding.categoryInput
        categorySpinner = binding.categorySpinner
        /*
         * spinner needs an adapter with the list of categories in the database
         */
        val categorySpinnerAdapter = ArrayAdapter<CategoryIdentity>(requireContext(), android.R.layout.simple_spinner_item)
        viewModel.loadCategoryList(categorySpinnerAdapter)
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.setAdapter(categorySpinnerAdapter)

        reference = binding.referenceInput
        meaning = binding.meaningInput

        submitButton = binding.addSymbolSubmit
        submitButton.setOnClickListener {
            viewModel.submitted(binding)
        }

    }

}