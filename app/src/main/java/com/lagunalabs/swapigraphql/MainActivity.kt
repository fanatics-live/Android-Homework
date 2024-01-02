package com.lagunalabs.swapigraphql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lagunalabs.swapigraphql.ui.theme.SWAPIGraphQLTheme

class MainActivity : ComponentActivity() {

    private val viewModel: PeopleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSheet by remember {
                mutableStateOf(false)
            }

            if (showSheet) {
                BottomSheet {
                    showSheet = false
                }
            }

            SWAPIGraphQLTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "people") {
                        composable("people") {
                            InitPeople(onNavigateToPerson = {
                                navController.navigate(
                                    "person/{id}".replace(
                                        oldValue = "{id}",
                                        newValue = it
                                    )
                                )
                            })
                        }
                        composable(
                            "person/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { navBackStackEntry ->
                            val personId = navBackStackEntry.arguments?.getString("id")
                            Person(personId = personId) {
                                showSheet = true
                            }
                        }
                    }
                }
            }
        }

        viewModel.fetchPeople()
    }

    @Composable
    fun InitPeople(onNavigateToPerson: (name: String) -> Unit) {
        Column {
            Text(
                stringResource(R.string.people_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.topColor))
                    .padding(16.dp, 24.dp, 16.dp, 24.dp),
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .gradientBackground(
                    colorResource(R.color.topColor),
                    colorResource(R.color.bottomColor)
                )
        ) {
            items(people.size) { index ->
                people[index]?.apply {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp, 16.dp, 0.dp)
                    ) {
                        Text(
                            name.toString(),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Image(painter = painterResource(id = R.drawable.ic_chevron_right_white),
                            contentDescription = "click to Person",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    onNavigateToPerson(id)
                                })
                    }
                    height?.let {
                        Text(
                            stringResource(R.string.height_label, it),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        )
                    }
                    mass?.let {
                        Text(
                            stringResource(R.string.mass_label, it),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        )
                    }
                    if (index != people.lastIndex) {
                        Divider(
                            color = Color.White, thickness = 1.dp, modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 16.dp, 0.dp, 0.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Person(personId: String?, onShowSheet: () -> Unit) {
        val person = viewModel.getPersonFromMemory(personId)

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.topColor))
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back_white),
                    contentDescription = "click to Person",
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(
                    stringResource(id = R.string.people_title),
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
            }
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
                            viewModel.updateHomeWorld(person?.homeworld)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .gradientBackground(
                        colorResource(R.color.topColor),
                        colorResource(R.color.bottomColor)
                    )
                    .padding(16.dp)
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
        val planet by remember { viewModel.homeWorldState }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gradientBackground(
                    colorResource(id = R.color.topColor),
                    colorResource(id = R.color.bottomColor)
                )
                .padding(16.dp)
        ) {
            planet?.apply {
                Text(
                    name.toString(),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp, 0.dp, 0.dp)
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