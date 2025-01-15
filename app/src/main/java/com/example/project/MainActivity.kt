package com.example.project

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tac_2025_booksfinder.ui.theme.ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTheme {
                MyApp(provideBooksViewModel())}
        }
    }


    @Composable
    fun provideBooksViewModel(): BooksViewModel {
        val application = LocalContext.current.applicationContext as Application
        return viewModel(factory = BooksViewModelFactory(application))
    }
}
