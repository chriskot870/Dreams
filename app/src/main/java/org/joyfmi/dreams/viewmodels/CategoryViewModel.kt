package org.joyfmi.dreams.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.repository.Category

class CategoryViewModel(private val repository:DreamRepository): ViewModel() {

    fun getallCategories(): Flow<List<Category>>? = repository.getAllCategories()

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