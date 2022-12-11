/*
package com.csi4999.visionaryalarmclock.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_news.*
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.database.ArticleDatabase
import com.csi4999.visionaryalarmclock.model.NewsViewModel
import com.csi4999.visionaryalarmclock.model.NewsViewModelProviderFactory
import com.csi4999.visionaryalarmclock.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newNavHostFragment.findNavController())
    }
}*/
