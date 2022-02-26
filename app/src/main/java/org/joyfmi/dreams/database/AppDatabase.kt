package org.joyfmi.dreams.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.joyfmi.dreams.database.category.Category
import org.joyfmi.dreams.database.category.CategoryDao
import org.joyfmi.dreams.database.symbol.Symbol
import org.joyfmi.dreams.database.symbol.SymbolDao


@Database(entities = arrayOf(Category::class, Symbol::class), version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun symbolDao(): SymbolDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "dreams")
                    .createFromAsset("database/dreams.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}