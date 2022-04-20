package org.joyfmi.dreams.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.repository.*
import org.joyfmi.dreams.ui.MeaningListAdapter

class MeaningViewModel(private val repository: DreamRepository): ViewModel() {

    //fun getMeaningBySymbolId(): Flow<List<Meaning>>? = repository.getAllCategories()

    //fun getMeaningsBySymbolIdentity(symbolIdentity: SymbolIdentity): Flow<List<Meaning>> = repository.meaningsBySymbolIdentity(symbolIdentity)

    fun loadSymbolMeanings(symbolIdentity: SymbolIdentity, meaningAdapter: MeaningListAdapter) {
        /*
         * We launch a coroutine to get the meanings for the Symbol. The meaningsBySymbolIdentity()
         * will use withContext(Dispacthers.IO). WHen it comes back we will be in main and we can
         * submit the list.
         */
        viewModelScope.launch() {
            val meaningList = repository.meaningsBySymbolIdentity(symbolIdentity)
            meaningAdapter.submitList(meaningList)
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