package com.lagunalabs.swapigraphql.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import com.lagunalabs.swapigraphql.R
import com.lagunalabs.swapigraphql.ui.theme.MidnightBlue

@Composable
fun PeopleListScreen(
    persons: List<GetPeopleQuery.Person>,
    navController: NavHostController
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(MidnightBlue, Color.Black)
    )
    val paddingMedium = dimensionResource(id = R.dimen.padding_medium)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient),
    ) {
        Column(Modifier.background(brush = gradient)) {
            Row() {
                Text(
                    text = "People",
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    modifier = Modifier.padding(paddingMedium),
                    color = Color.White
                )
            }
            Divider(color = Color.Gray)

            PersonList(persons = persons, navController = navController)
        }
    }
}

@Composable
fun PersonList(persons: List<GetPeopleQuery.Person>, navController: NavController) {
    val paddingMedium = dimensionResource(id = R.dimen.padding_medium)

    Box(modifier = Modifier.fillMaxSize()) {
        if (persons.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
        ) {
            itemsIndexed(persons) { index, person ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent)
                        .padding(paddingMedium)
                        .clickable {
                            navController.navigate("peopleDetail/$index")
                        }
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Name: ${person.name}", color = Color.White)
                        Text(
                            text = "Height: ${person.height?.toString() ?: "Unknown"}",
                            color = Color.White
                        )
                        Text(
                            text = "Mass: ${person.mass?.toString() ?: "Unknown"}",
                            color = Color.White
                        )
                    }

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.align(
                            Alignment.CenterVertically
                        ),
                        tint = Color.White
                    )
                }
                Divider(color = Color.Gray)
            }
        }
    }
}