package com.lagunalabs.swapigraphql.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lagunalabs.swapigraphql.ui.theme.MidnightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleDetailScreen(person: GetPeopleQuery.Person, navController: NavController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(MidnightBlue, Color.Black)
    )

    val showDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ) {
        Column() {
            TopAppBar(
                title = { Text(text = "Person") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )

            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White, fontSize = 26.sp)) {
                    append("Click")
                }
                pushStyle(
                    SpanStyle(
                        color = Color.Blue,
                        fontSize = 26.sp,
                        textDecoration = TextDecoration.Underline
                    )
                )
                append(" here ")
                pop()
                withStyle(style = SpanStyle(color = Color.White, fontSize = 26.sp)) {
                    append("to view home data for ${person.name} ")
                }
            }

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    showDialog.value = true
                },
                modifier = Modifier.padding(16.dp)
            )

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text(text = "Dialog Title") },
                    text = { Text(text = "Dialog Description") },
                    confirmButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

// according to stackoverflow answers, this is the way to 
// implement a bottom sheet before compose material3 bottom sheet final released
//https://stackoverflow.com/questions/72518262/how-to-implement-bottomsheet-in-material-3-jetpack-compose-android

