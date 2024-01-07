package com.lagunalabs.swapigraphql

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import com.lagunalabs.swapigraphql.networking.ApolloNetworking
import com.lagunalabs.swapigraphql.ui.theme.SWAPIGraphQLTheme
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lagunalabs.swapigraphql.ui.PeopleDetailScreen
import androidx.navigation.compose.rememberNavController
import com.lagunalabs.swapigraphql.ui.PeopleListScreen

class MainActivity : ComponentActivity() {
    companion object {
        private val networking by lazy { ApolloNetworking() }
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Remove the flash that appears during page switching
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContent {
            SWAPIGraphQLTheme {
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                var persons by remember { mutableStateOf(listOf<GetPeopleQuery.Person>()) }

                LaunchedEffect(Unit) {
                    scope.launch {
                        val response = runCatching {
                            networking.fetch(GetPeopleQuery())
                        }

                        response.onFailure {
                            Log.e(TAG, it.message ?: it.toString())
                        }

                        response.onSuccess {
                            persons = it.allPeople?.people?.filterNotNull() ?: listOf()
                        }
                    }
                }

                MainNavHost(navController = navController, persons)
            }
        }
    }
}