package com.lagunalabs.swapigraphql.networking

import com.lagunalabs.`swapi-graphql`.GetPeopleQuery

class PeopleApiService {

    private val networking by lazy { ApolloNetworking() }

    suspend fun getPeople(): List<GetPeopleQuery.Person?>? =
        networking.fetch(GetPeopleQuery()).allPeople?.people
}