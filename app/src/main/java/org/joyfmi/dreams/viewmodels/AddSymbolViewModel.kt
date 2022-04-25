package org.joyfmi.dreams.viewmodels

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.R
import org.joyfmi.dreams.database.local.LocalMeaning
import org.joyfmi.dreams.databinding.AddSymbolFragmentBinding
import org.joyfmi.dreams.repository.CategoryIdentity
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.ui.AddSymbolDialogFragment
import org.joyfmi.dreams.ui.AddSymbolFragment
import org.joyfmi.dreams.ui.CategoryListAdapter

class AddSymbolViewModel (private val repository:DreamRepository): ViewModel() {
/*
    object storage {
        var symbolText: String = String()
        var categoryText: String = String()
        var categorySpinnerChoice = ""
        var referenceText = String()
        var contentsText = String()
    }
 */
    /*
     * Start a coroutine and get the list of CategoryIdentities
     * This routine is experimental
     */
    fun loadCategoryList(spinner: Spinner, arrayAdapter: ArrayAdapter<CategoryIdentity>, defChoice: String? = null) {
        /*
         * We are going to do some I/O so launch a coroutine to do the work
         */
        viewModelScope.launch() {
            val categories = repository.getAllCategories(Dispatchers.IO).toMutableList()

            /*
             * We want to remove the All value. So look through to find it.
             */
            for( n in 0 until categories.size) {
                    if (categories[n].name.equals("All")) {
                        categories.removeAt(n)
                        break
                    }
            }
            arrayAdapter.addAll(categories)

           /*
            * If default is not null then look for it and select it
            */
            if ( defChoice != null ) {
                for ( i in 0 until arrayAdapter.count ) {
                    if ( arrayAdapter.getItem(i)?.name.equals(defChoice) ) {
                        spinner.setSelection(i)
                        break
                    }
                }
            }
        }
    }

    fun submitted(binding: AddSymbolFragmentBinding, fragmentManager: FragmentManager) {

        val symbolName = binding.symbolInput.text.toString()
        if ( symbolName.isEmpty() ) {
            val alert = AddSymbolDialogFragment()
            alert.message = "Symbol can not be empty"
            alert.buttonLabel = "Try Again"
            alert.show(fragmentManager, "AddSymbolError")
            return
        }

        /*
         * If categoryInput is empty then get the category from the spinner
         * The spinner will always have a value
         */
        var categoryName = binding.categoryInput.text.toString()
        if ( categoryName.isEmpty() ) {
            categoryName = binding.categorySpinner.getSelectedItem().toString()
        }
        /*
         * Category Name can not be All
         */
        if ( categoryName.equals("All", ignoreCase = true) ) {
            val alert = AddSymbolDialogFragment()
            alert.message = "Category name cannot be All"
            alert.buttonLabel = "Try Again"
            alert.show(fragmentManager, "AddSymbolError")
            return
        }

        /*
         * Make sure the reference has a value
         */
        val referenceText = binding.referenceInput.text.toString()
        if ( referenceText.isEmpty() ) {
            val alert = AddSymbolDialogFragment()
            alert.message = "Reference can not be empty"
            alert.buttonLabel = "Try Again"
            alert.show(fragmentManager, "AddSymbolError")
            return
        }

        /*
         * Make sure that the contents is not empty
         */
        val contentsText = binding.meaningInput.text.toString()
        if ( contentsText.isEmpty() ) {
            val alert = AddSymbolDialogFragment()
            alert.message = "Contents can not be empty"
            alert.buttonLabel = "Try Again"
            alert.show(fragmentManager, "AddSymbolError")
            return
        }

        /*
         * The local value is always 1
         */
        val local = 1

        /*
         * We have to get the symbols to check if the one entered already exists.
         * This requires a database access so start a coroutine to get the symbols and do the rest
         * of the work while the original finishes this routine and becomes available to
         * respond to UI stuff that may be going on while we wait for a result
         */
        viewModelScope.launch {
            /*
             * repository.getAllSymbolNames operates issuing withContext(Dispatcher.IO)
             */
            var symbols = repository.getAllSymbolNames(Dispatchers.IO)
            symbols.forEach { symbol ->
                if (symbol.equals(symbolName, ignoreCase = true)) {
                    /*
                     * We have an error here so need to report it somehow
                     */
                    val alert = AddSymbolDialogFragment()
                    alert.message = "Symbol is already in use"
                    alert.buttonLabel = "Try Again"
                    alert.show(fragmentManager, "AddSymbolError")
                    return@launch
                }
            }
            /*
             * If we get here we have valid info so insert a new record into the Local Database
             */
            val newrecord = LocalMeaning(
                0,
                categoryName,
                symbolName,
                referenceText,
                contentsText,
                local
            )
            try {
                repository.localInsert(newrecord)
                val alert = AddSymbolDialogFragment()
                alert.message = "Success!"
                alert.buttonLabel = "OK"
                alert.show(fragmentManager, "AddSymbolError")
            } catch (e: Exception) {
                /*
                 * We have an error so report it
                 */
                val alert = AddSymbolDialogFragment()
                alert.message = "Error Inserting record into Database: " + e.message
                alert.buttonLabel = "OK"
                alert.show(fragmentManager, "AddSymbolError")
            }
        }
    }
}

class AddSymbolViewModelFactory(
    private val repository: DreamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSymbolViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddSymbolViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

