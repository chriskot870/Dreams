package org.joyfmi.dreams.viewmodels

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.repository.SymbolIdentity
import org.joyfmi.dreams.ui.CategoryListAdapter

class CategoryViewModel(private val repository:DreamRepository): ViewModel() {
    /*
     * Start a coroutine and get the list of CategoryIdentities
     * This routine is experimental
     */
    fun loadCategoryList(categoryAdapter: CategoryListAdapter) {
        /*
         * We launch a coroutine. We expect repository.getAllCategories to do all the database
         * work in to Dispatch.IO. We will be in Main when we get back. So, we can submit the list.
         */
        viewModelScope.launch() {
            val categories = repository.getAllCategories()
            categoryAdapter.submitList(categories)
        }
    }
    fun loadSymbolList(symbolNameAdapter: ArrayAdapter<SymbolIdentity>) {
        /*
         * We launch a coroutine. We expect repository.symbolIdentites to do all the database
         * work in to Dispatch.IO. We will be in Main when we get back. So, we can submit the list.
         */
        viewModelScope.launch(Dispatchers.IO) {
            val symbolList = repository.symbolIdentities()
            symbolNameAdapter.clear()
            symbolNameAdapter.addAll(symbolList)
        }
    }
}

class CategoryViewModelFactory(
    private val repository: DreamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}