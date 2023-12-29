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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lagunalabs.swapigraphql.ui.theme.SWAPIGraphQLTheme

class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val viewModel: PeopleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                                    "person/{name}".replace(
                                        oldValue = "{name}",
                                        newValue = it
                                    )
                                )
                            })
                        }
                        composable(
                            "person/{name}",
                            arguments = listOf(navArgument("name") { type = NavType.StringType })
                        ) { navBackStackEntry ->
                            val name = navBackStackEntry.arguments?.getString("name")
                            Person(name = name)
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
                getString(R.string.people_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(ContextCompat.getColor(this@MainActivity, R.color.topColor)))
                    .padding(16.dp, 24.dp, 16.dp, 24.dp),
                fontSize = 30.sp,
                color = Color.White
            )
            Divider(color = Color.Gray, thickness = 1.dp)
            People(onNavigateToPerson)
        }
    }

    @Composable
    fun People(onNavigateToPerson: (name: String) -> Unit) {
        val people by remember { viewModel.state }

        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                Color(ContextCompat.getColor(this, R.color.topColor)),
                Color(ContextCompat.getColor(this, R.color.bottomColor))
            ),
            startY = 0f,
            endY = 3000f
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
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
                                    onNavigateToPerson(name.toString())
                                })
                    }
                    height?.let {
                        Text(
                            getString(R.string.height_label, it),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                        )
                    }
                    mass?.let {
                        Text(
                            getString(R.string.mass_label, it),
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
    fun Person(name: String?) {
        Text(getString(R.string.person_prompt, name))
    }
}