package com.lagunalabs.swapigraphql

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import com.lagunalabs.swapigraphql.ui.PeopleDetailScreen
import com.lagunalabs.swapigraphql.ui.PeopleListScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    persons: List<GetPeopleQuery.Person>
) {

    fun slideInTransition() = slideInHorizontally(
        initialOffsetX = { 1000 },
        animationSpec = tween(300)
    )

    fun slideOutTransition() = slideOutHorizontally(
        targetOffsetX = { 1000 },
        animationSpec = tween(300)
    )


    NavHost(navController, startDestination = "personList") {

        composable("personList",
        ) {
            PeopleListScreen(persons, navController)
        }

        composable("peopleDetail/{index}",
            enterTransition = { slideInTransition() },
            exitTransition = { slideOutTransition() },
            
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0
            PeopleDetailScreen(persons.get(index), navController)
        }
    }
}