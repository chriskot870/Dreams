package org.joyfmi.dreams.viewmodels

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.database.local.LocalMeaning
import org.joyfmi.dreams.databinding.AddSymbolFragmentBinding
import org.joyfmi.dreams.repository.CategoryIdentity
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.ui.CategoryListAdapter

class AddSymbolViewModel (private val repository:DreamRepository): ViewModel() {

    object storage {
        var symbolText: String = String()
        var categoryText: String = String()
        var categorySpinnerChoice = ""
        var referenceText = String()
        var contentsText = String()
    }
    /*
     * Start a coroutine and get the list of CategoryIdentities
     * This routine is experimental
     */
    fun loadCategoryList(arrayAdapter: ArrayAdapter<CategoryIdentity>) {
        /*
         * We are going to do some I/O so launch a coroutine to do the work
         */
        viewModelScope.launch() {
            val categories = repository.getAllCategories()
            arrayAdapter.addAll(categories)
        }
    }
    fun submitted(binding: AddSymbolFragmentBinding) {

        AddSymbolViewModel.storage.categoryText = binding.categoryInput.text.toString()
        AddSymbolViewModel.storage.symbolText = binding.symbolInput.text.toString()
        AddSymbolViewModel.storage.referenceText = binding.referenceInput.text.toString()
        storage.contentsText = binding.meaningInput.text.toString()
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
            var symbols = repository.getAllSymbolNames()
            if ( symbols.contains(storage.symbolText) ) {
                /*
                 * We have an error here so need to report it somehow
                 */
            }
            /*
             * If categoryInput is empty then get the category from the spinner
             * The spinner will always have a value
             */
            lateinit var categoryName: String
            if ( storage.categoryText.isEmpty() ) {
                categoryName = storage.categorySpinnerChoice
            }
            else {
                categoryName = storage.categoryText
            }
            /*
             * Make sure the reference has a value
             */
            if ( storage.referenceText.isEmpty() ) {
                /*
                 * Error that reference can't be empty
                 */
            }
            /*
             * Make sure that the contents is not empty
             */
            if ( storage.contentsText.isEmpty() ) {
                /*
                 * Error that reference can't be empty
                 */
            }
            /*
             * If we get here we have valid info so insert a new record into the Local Database
             */
            val newrecord = LocalMeaning(
                0,
                storage.symbolText,
                categoryName,
                storage.referenceText,
                storage.contentsText,
                local
            )
            repository.localInsert(newrecord)
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