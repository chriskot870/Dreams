package org.joyfmi.dreams.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.ui.CategoryListAdapter

class CategoryViewModel(private val repository:DreamRepository): ViewModel() {
    /*
     * Start a coroutine and get the list of CategoryIdentities
     * This routine is experimental
     */
    fun loadCategoryList(categoryAdapter: CategoryListAdapter) {
        /*
         * We are going to do some I/O so launch a coroutine to do the work
         */
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllCategories().collect {
                /*
                 * We need to go into the main thread in order to update the symbolAdapter List
                 */
                withContext(Dispatchers.Main) {
                    categoryAdapter.submitList(it)
                }
            }
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