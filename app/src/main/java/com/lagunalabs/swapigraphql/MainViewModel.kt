package com.lagunalabs.swapigraphql

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lagunalabs.swapigraphql.networking.ApolloNetworking
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val networking by lazy { ApolloNetworking() }
    val persons = mutableStateOf(listOf<GetPeopleQuery.Person>())
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    init {
        fetchPeople()
    }

    private fun fetchPeople() {
        viewModelScope.launch {
            isLoading.value = true
            val response = runCatching {
                networking.fetch(GetPeopleQuery())
            }
            isLoading.value = false

            response.onFailure {
                errorMessage.value = it.message ?: "Unknown error"
            }

            response.onSuccess {
                persons.value = it.allPeople?.people?.filterNotNull() ?: listOf()
            }
        }
    }
}