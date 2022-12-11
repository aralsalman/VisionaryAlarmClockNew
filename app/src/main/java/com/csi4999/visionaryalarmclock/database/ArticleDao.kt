/*
package com.csi4999.visionaryalarmclock.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.csi4999.visionaryalarmclock.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long //returns the id's of the article

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}*/
