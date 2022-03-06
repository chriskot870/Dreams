package org.joyfmi.dreams.database.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = arrayOf(CommonCategory::class, CommonSymbol::class, CommonMeaning::class),
    version = 1,
    exportSchema = false)
abstract class CommonDatabase: RoomDatabase() {

    abstract fun commonCategoryDao(): CommonCategoryDao
    abstract fun commonSymbolDao(): CommonSymbolDao
    abstract fun commonMeaningDao(): CommonMeaningDao

    companion object {
        @Volatile
        private var INSTANCE: CommonDatabase? = null

        fun getDatabase(context: Context): CommonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    CommonDatabase::class.java,
                    "dreams")
                    .createFromAsset("database/dreams.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}