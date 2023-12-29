package com.lagunalabs.swapigraphql

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import com.lagunalabs.swapigraphql.networking.PeopleApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PeopleViewModel: ViewModel() {

    val state: MutableState<List<GetPeopleQuery.Person?>> = mutableStateOf(emptyList())

    private val apiService by lazy { PeopleApiService() }

    fun fetchPeople() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                apiService.getPeople()
            }.onSuccess { people ->
                people?.let { state.value = it }
            }.onFailure {
                Log.e(MainActivity.TAG, it.message ?: it.toString())
            }
        }
    }
}