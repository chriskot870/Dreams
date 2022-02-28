package org.joyfmi.dreams.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.repository.Meaning



class MeaningViewModel(private val repository: DreamRepository): ViewModel() {

    //fun getMeaningBySymbolId(): Flow<List<Meaning>>? = repository.getAllCategories()

    fun getMeaningsBySymbol(symbol: String): Flow<List<Meaning>>? = repository.getMeaningsBySymbolName(symbol)
}

class MeaningViewModelFactory(
    private val repository: DreamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeaningViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeaningViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}