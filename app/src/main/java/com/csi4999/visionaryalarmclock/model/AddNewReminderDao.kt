package com.csi4999.visionaryalarmclock.model

import androidx.room.*

@Dao
interface AddNewReminderDao {

    @Insert
    fun insertAll(addNewReminderTable: AddNewReminderTable):Long

    @Query("SELECT * FROM AddNewReminderTable")
    fun fetchAllData(): List<AddNewReminderTable>

    @Query("SELECT * FROM AddNewReminderTable WHERE isFav=:isFav")
    fun fetchFavouritesReminder(isFav:Boolean): List<AddNewReminderTable>

    @Query("SELECT * FROM AddNewReminderTable WHERE isDone=:isDone")
    fun fetchDoneReminder(isDone:Boolean): List<AddNewReminderTable>

    @Query("SELECT * FROM AddNewReminderTable WHERE date=:date")
    fun fetchDateWiseReminder(date:String): List<AddNewReminderTable>

    @Update
    fun updateAll(addNewReminderTable: AddNewReminderTable):Int

    @Delete
    fun deleteData(addNewReminderTable: AddNewReminderTable): Int
}