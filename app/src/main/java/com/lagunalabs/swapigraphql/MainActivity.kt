package com.lagunalabs.swapigraphql

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
                
                NavHost(navController, startDestination = "personList") {
                    composable("personList") {
                        PeopleListScreen(persons, navController)
                    }

                    composable("peopleDetail/{index}") { backStackEntry ->
                        val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0
                        PeopleDetailScreen(persons.get(index), navController)
                    }
                }
            }
        }
    }
}