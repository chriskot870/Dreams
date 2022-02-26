package org.joyfmi.dreams.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.joyfmi.dreams.database.category.Category
import org.joyfmi.dreams.database.category.CategoryDao
import kotlinx.coroutines.flow.Flow

class CategoryViewModel(private val categoryDao: CategoryDao): ViewModel() {

    fun allCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
}

class CategoryViewModelFactory(
    private val categoryDao: CategoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(categoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}