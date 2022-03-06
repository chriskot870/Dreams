package org.joyfmi.dreams.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.joyfmi.dreams.repository.*
import org.joyfmi.dreams.ui.MeaningListAdapter
import org.joyfmi.dreams.ui.SymbolListAdapter

class MeaningViewModel(private val repository: DreamRepository): ViewModel() {

    //fun getMeaningBySymbolId(): Flow<List<Meaning>>? = repository.getAllCategories()

    fun getMeaningsBySymbolIdentity(symbolIdentity: SymbolIdentity): Flow<List<Meaning>> = repository.getMeaningsBySymbolIdentity(symbolIdentity)

    fun loadSymbolMeanings(symbolIdentity: SymbolIdentity, meaningAdapter: MeaningListAdapter) {
        /*
         * Start a coroutine and get the list of CategoryIdentities
         */
        Log.d("Dreams", "Entered loadSymbolMeanings")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Dreams", "In new launch")
            repository.getMeaningsBySymbolIdentity(symbolIdentity).collect() {
                meaningAdapter.submitList(it)
            }
            Log.d("Dreams", "Exiting loadSymbolMeanings")
        }
    }
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