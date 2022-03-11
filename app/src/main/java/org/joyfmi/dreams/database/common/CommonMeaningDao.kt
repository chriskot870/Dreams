package org.joyfmi.dreams.database.common

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
 * This provides the routines to get data from the Symbol Table of the database
 */
@Dao
interface CommonMeaningDao {
    /*
     * Get the Meaning with the provided Id from the Meaning table of the database
     * This will get a single entry
     */
    @Query("SELECT * FROM Meaning WHERE Id = :id")
    fun getMeaningById(id: Int): CommonMeaning
    /*
     * Get the Meanings with the provided symbolID from the Meaning table of the database
     * Currently we only expect one entry but the database can handle multiple meanings
     * for the same symbolId. So, go ahead and support multiple results now so we can just
     * update the database without having to come back here and support multiple entries.
     * So we expect a List of Meanings, but currently expect to just have one element on the List
     */
    @Query("SELECT * FROM Meaning WHERE symbolId = :symId")
    fun getMeaningsBySymbolId(symId: Int): List<CommonMeaning>

}
