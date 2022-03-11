package org.joyfmi.dreams.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.repository.*
import org.joyfmi.dreams.ui.MeaningListAdapter
import org.joyfmi.dreams.ui.SymbolListAdapter

class MeaningViewModel(private val repository: DreamRepository): ViewModel() {

    //fun getMeaningBySymbolId(): Flow<List<Meaning>>? = repository.getAllCategories()

    fun getMeaningsBySymbolIdentity(symbolIdentity: SymbolIdentity): Flow<List<Meaning>> = repository.meaningsBySymbolIdentity(symbolIdentity)

    fun loadSymbolMeanings(symbolIdentity: SymbolIdentity, meaningAdapter: MeaningListAdapter) {
        /*
         * We are going to do some I/O so launch a coroutine to do the work
         */
        viewModelScope.launch(Dispatchers.IO) {
            getMeaningsBySymbolIdentity(symbolIdentity).collect() {
                /*
                 * We need to go into the main thread in order to update the meaningAdapter List
                 */
                withContext(Dispatchers.Main) {
                    meaningAdapter.submitList(it)
                }
            }
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