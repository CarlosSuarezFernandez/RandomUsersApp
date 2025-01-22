package com.carlosdev.randomusersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlosdev.randomusersapp.domain.usecase.GetRandomUsersUsecase
import com.carlosdev.randomusersapp.presentation.MainScreen
import com.carlosdev.randomusersapp.presentation.UsersList
import com.carlosdev.randomusersapp.presentation.theme.RandomUsersAppTheme
import kotlinx.coroutines.coroutineScope
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUsersAppTheme {
                MainScreen()
            }
        }
    }
}