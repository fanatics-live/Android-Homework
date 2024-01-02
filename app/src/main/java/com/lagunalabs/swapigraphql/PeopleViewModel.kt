package com.lagunalabs.swapigraphql

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import com.lagunalabs.`swapi-graphql`.GetPersonQuery
import com.lagunalabs.swapigraphql.networking.PeopleApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PeopleViewModel: ViewModel() {

    val peopleState: MutableState<List<GetPeopleQuery.Person?>> = mutableStateOf(emptyList())

    val personState: MutableState<GetPersonQuery.Person?> = mutableStateOf(null)

    private val apiService by lazy { PeopleApiService() }

    private val tag = "PeopleViewModel"

    fun fetchPeople() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                apiService.getPeople()
            }.onSuccess { people ->
                people?.let { peopleState.value = it }
            }.onFailure {
                Log.e(tag, it.message ?: it.toString())
            }
        }
    }

    fun fetchPerson(personId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                apiService.getPerson(personId)
            }.onSuccess {
                personState.value = it
            }.onFailure {
                Log.e(tag, it.message ?: it.toString())
            }
        }
    }
}