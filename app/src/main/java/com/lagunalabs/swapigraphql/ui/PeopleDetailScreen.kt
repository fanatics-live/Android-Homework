package com.lagunalabs.swapigraphql.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.lagunalabs.swapigraphql.ui.bottomsheet.CustomBottomSheetContainer
import com.lagunalabs.swapigraphql.ui.theme.MidnightBlue
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import com.lagunalabs.swapigraphql.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleDetailScreen(person: GetPeopleQuery.Person, navController: NavController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(MidnightBlue, Color.Black)
    )
    val paddingMedium = dimensionResource(id = R.dimen.padding_medium)

    val showButtonSheet = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ) {
        Column {
            TopAppBar(
                title = { Text(text = "Person") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                )
            )

            ClickableText(
                text = annotatedString(person),
                onClick = {
                    showButtonSheet.value = true
                },
                modifier = Modifier.padding(paddingMedium)
            )

            if (showButtonSheet.value) {
                buttonSheet(showButtonSheet)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun buttonSheet(showButtonSheet: MutableState<Boolean>) {
    val paddingMedium = dimensionResource(id = R.dimen.padding_medium)
    ModalBottomSheet(
        modifier = Modifier,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        ),
        dragHandle = null,
        onDismissRequest = {
            showButtonSheet.value = false
        },
        shape = RoundedCornerShape(
            topStart = paddingMedium,
            topEnd = paddingMedium
        ),
    ) {
        // seems network call is not return homepage data, so I just hardcode the url
        CustomBottomSheetContainer("https://swapi.dev/api/planets/1/")
    }
}

@Composable
private fun annotatedString(person: GetPeopleQuery.Person): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White, fontSize = MaterialTheme.typography.headlineSmall.fontSize)) {
            append("Click")
        }
        pushStyle(
            SpanStyle(
                color = Color.Blue,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                textDecoration = TextDecoration.Underline
            )
        )
        append(" here ")
        pop()
        withStyle(style = SpanStyle(color = Color.White, fontSize = MaterialTheme.typography.headlineSmall.fontSize)) {
            append("to view homeworld data for ${person.name} ")
        }
    }
    return annotatedString
}

