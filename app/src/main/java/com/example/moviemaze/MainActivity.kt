package com.example.moviemaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviemaze.movielist.presentation.MovieListViewModel
import com.example.moviemaze.ui.theme.MoviemazeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviemazeTheme {
                ChangeBarTheme(!isSystemInDarkTheme())
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val movieListViewModel = hiltViewModel<MovieListViewModel>()
                }
            }
        }
    }
    @Composable
    fun ChangeBarTheme(theme : Boolean) {
        val barColor = MaterialTheme.colorScheme.background.toArgb()
        LaunchedEffect(theme) {
            if (theme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        barColor, barColor
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        barColor, barColor
                    )
                )
            }
            else {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(
                        barColor
                    ),
                    navigationBarStyle = SystemBarStyle.dark(
                        barColor
                    )
                )
            }
        }
    }
}