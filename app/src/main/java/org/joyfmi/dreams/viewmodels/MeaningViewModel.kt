package org.joyfmi.dreams.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import org.joyfmi.dreams.database.meaning.Meaning
import org.joyfmi.dreams.database.meaning.MeaningDao


class MeaningViewModel(private val meaningDao: MeaningDao): ViewModel() {

    fun getMeaningBySymbolId(id: Int): Flow<List<Meaning>> = meaningDao.getMeaningBySymbolId(id)
}

class MeaningViewModelFactory(
    private val meaningDao: MeaningDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeaningViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeaningViewModel(meaningDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}