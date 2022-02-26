package org.joyfmi.dreams.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.joyfmi.dreams.database.symbol.Symbol
import org.joyfmi.dreams.database.symbol.SymbolDao
import kotlinx.coroutines.flow.Flow

class SymbolViewModel(private val symbolDao: SymbolDao): ViewModel() {

    fun symbolsByCategoryId(categoryId: Int): Flow<List<Symbol>> = symbolDao.getSymbolsByCategoryId(categoryId)
}

class SymbolViewModelFactory(
    private val symbolDao: SymbolDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SymbolViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SymbolViewModel(symbolDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}