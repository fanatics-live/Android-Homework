package com.lagunalabs.swapigraphql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lagunalabs.swapigraphql.ui.theme.SWAPIGraphQLTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PeopleViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSheet by remember { mutableStateOf(false) }

            LaunchedEffect(true) {
                viewModel.fetchPeople()
            }

            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val showBackButton by remember(currentBackStackEntry) {
                derivedStateOf { navController.previousBackStackEntry != null }
            }

            SWAPIGraphQLTheme {
                Surface {
                    Scaffold(
                        modifier = Modifier
                            .gradientBackground(
                                colorResource(R.color.topColor),
                                colorResource(R.color.bottomColor)
                            ),
                        topBar = {
                            if (showBackButton) {
                                TopAppBar(
                                    title = { Text(stringResource(id = R.string.people_title), color = Color.White) },
                                    navigationIcon = {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color.Transparent
                                    )
                                )
                            }
                        },
                        containerColor = Color.Transparent
                    ) { scaffoldPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "people"
                        ) {
                            composable(
                                route = "people",
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                        animationSpec = tween(400)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                        animationSpec = tween(400)
                                    )
                                },
                                popEnterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                        animationSpec = tween(400)
                                    )
                                },
                                popExitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                        animationSpec = tween(400)
                                    )
                                }
                            ) {
                                InitPeople(
                                    onNavigateToPerson = {
                                        navController.navigate(
                                            "person/{id}".replace(
                                                oldValue = "{id}",
                                                newValue = it
                                            )
                                        )
                                    }
                                )
                            }

                            composable(
                                route = "person/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                        animationSpec = tween(400)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                        animationSpec = tween(400)
                                    )
                                },
                                popEnterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                        animationSpec = tween(400)
                                    )
                                },
                                popExitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                        animationSpec = tween(400)
                                    )
                                }
                            ) { navBackStackEntry ->
                                val personId = navBackStackEntry.arguments?.getString("id")

                                Person(
                                    modifier = Modifier.padding(scaffoldPadding),
                                    personId = personId
                                ) {
                                    showSheet = true
                                }
                            }
                        }
                    }
                }
            }

            if (showSheet) {
                BottomSheet {
                    showSheet = false
                }
            }
        }
    }

    @Composable
    fun InitPeople(modifier: Modifier = Modifier, onNavigateToPerson: (name: String) -> Unit) {
        Column(modifier = modifier) {
            Text(
                stringResource(R.string.people_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                fontSize = 30.sp,
                color = Color.White
            )
            Divider(color = Color.Gray, thickness = 1.dp)
            People(onNavigateToPerson)
        }
    }

    @Composable
    fun People(onNavigateToPerson: (personId: String) -> Unit) {
        val people by remember { viewModel.peopleState }

        LazyColumn {
            itemsIndexed(items = people.mapNotNull { it }) { index, person ->
                person.apply {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                onNavigateToPerson(id)
                            }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                name.toString(),
                                color = Color.White
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_chevron_right_white),
                                contentDescription = "click to Person",
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }

                        height?.let {
                            Text(
                                stringResource(R.string.height_label, it),
                                color = Color.White,
                            )
                        }
                        mass?.let {
                            Text(
                                stringResource(R.string.mass_label, it),
                                color = Color.White
                            )
                        }
                    }

                    if (index != people.size - 1) {
                        Divider(
                            color = Color.White,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Person(
        modifier: Modifier = Modifier,
        personId: String?,
        onShowSheet: () -> Unit
    ) {
        LaunchedEffect(true) {
            viewModel.fetchPerson(personId)
        }

        val person by remember { viewModel.personState }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val clickableText = "here"
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White, fontSize = 24.sp)) {
                    append("Click ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    pushStringAnnotation(tag = clickableText, annotation = clickableText)
                    append(clickableText)
                }
                withStyle(style = SpanStyle(color = Color.White, fontSize = 24.sp)) {
                    append(" to view homeworld data for ${person?.name}")
                }
            }

            ClickableText(
                text = annotatedString,
                onClick = {
                    annotatedString.getStringAnnotations(it, it).firstOrNull()?.let { range ->
                        if (range.item == clickableText) {
                            onShowSheet()
                        }
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomSheet(onDismiss: () -> Unit) {
        val modalBottomSheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            HomeWorld()
        }
    }

    @Composable
    fun HomeWorld() {
        val person by remember { viewModel.personState }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gradientBackground(
                    colorResource(id = R.color.topColor),
                    colorResource(id = R.color.bottomColor)
                )
                .padding(16.dp)
        ) {
            person?.homeworld?.apply {
                Text(
                    name.toString(),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                population?.let { Text(text = "Population: $it", color = Color.White) }
                diameter?.let { Text(text = "Diameter: $it", color = Color.White) }
                gravity?.let { Text(text = "Gravity: $it", color = Color.White) }
            }
        }
    }

    private fun Modifier.gradientBackground(top: Color, bottom: Color): Modifier =
        this.background(
            brush = Brush.verticalGradient(
                colors = listOf(top, bottom),
                startY = 0f,
                endY = 3000f
            )
        )
}