package org.joyfmi.dreams.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.repository.CategoryIdentity
import org.joyfmi.dreams.repository.DreamRepository
import org.joyfmi.dreams.repository.SymbolIdentity
import org.joyfmi.dreams.ui.SymbolListAdapter

class SymbolViewModel(private val repository: DreamRepository): ViewModel() {

    suspend fun symbolIdentitiesByCategoryIdentity(categoryIdentity: CategoryIdentity): Flow<List<SymbolIdentity>> = repository.symbolIdentitiesByCategoryIdentity(categoryIdentity)

    fun loadSymbolList(categoryIdentity: CategoryIdentity,  symbolAdapter: SymbolListAdapter) {
        /*
         * We are going to do some I/O so launch a coroutine to do the work
         */
        viewModelScope.launch(Dispatchers.IO) {
            repository.symbolIdentitiesByCategoryIdentity(categoryIdentity).collect() {
                /*
                 * We need to go into the main thread in order to update the symbolAdapter List
                 */
                withContext(Dispatchers.Main) {
                    symbolAdapter.submitList(it)
                }
            }
        }
    }
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