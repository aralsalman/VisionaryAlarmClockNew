package com.csi4999.visionaryalarmclock.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.csi4999.visionaryalarmclock.model.AddNewReminderDao
import com.csi4999.visionaryalarmclock.model.AddNewReminderTable
import com.csi4999.visionaryalarmclock.util.DATABASE_NAME

@Database(entities = [AddNewReminderTable::class],version = 6, exportSchema = false)
abstract class ReminderDatabase: RoomDatabase(){

    abstract fun addNewReminderDao():AddNewReminderDao

    companion object {
        @Volatile private var instance: ReminderDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            ReminderDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}