package org.joyfmi.dreams.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.repository.CategoryIdentity
import org.joyfmi.dreams.ui.CategoryListAdapter

class CategoryViewModel(private val repository:DreamRepository): ViewModel() {

    suspend fun getAllCategories(): Flow<List<CategoryIdentity>> {
        Log.d("Category","In ViewModel calling repository" )
        val catList = repository.getAllCategories()
        Log.d("Category", "ViewModel back fro Repository")
        return catList
    }
    /*
     * Start a coroutine and get the list of CategoryIdentities
     * This routine is experimental
     */
    fun loadCategoryList(categoryAdapter: CategoryListAdapter) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllCategories().collect() {
                categoryAdapter.submitList(it)
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