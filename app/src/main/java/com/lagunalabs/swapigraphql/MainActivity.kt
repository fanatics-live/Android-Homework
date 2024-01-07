package com.lagunalabs.swapigraphql

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lagunalabs.swapigraphql.ui.theme.SWAPIGraphQLTheme
import androidx.navigation.compose.rememberNavController
import com.lagunalabs.swapigraphql.ui.theme.Black
import com.lagunalabs.swapigraphql.ui.theme.MidnightBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove the flash that appears during page switching
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val gradient = Brush.verticalGradient(
            colors = listOf(MidnightBlue, Black)
        )

        setContent {
            SWAPIGraphQLTheme {
                val viewModel: MainViewModel = viewModel()
                if (viewModel.errorMessage.value.isNotEmpty()) {
                    // Show error message
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(brush = gradient)) {
                        Text(text = viewModel.errorMessage.value)
                    }
                } else {
                    // Show content
                    MainNavHost(
                        navController = rememberNavController(),
                        persons = viewModel.persons.value
                    )
                }
            }
        }
    }
}