package org.joyfmi.dreams.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.repository.Symbol

class SymbolViewModel(private val repository: DreamRepository): ViewModel() {

    fun symbolsByCategoryId(categoryId: Int): Flow<List<Symbol>>? = repository.symbolsByCategoryId(categoryId)
}

class SymbolViewModelFactory(
    private val repository: DreamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SymbolViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SymbolViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}