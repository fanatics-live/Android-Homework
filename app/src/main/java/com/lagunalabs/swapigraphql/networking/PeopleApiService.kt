package com.lagunalabs.swapigraphql.networking

import com.apollographql.apollo3.api.Optional
import com.lagunalabs.`swapi-graphql`.GetPeopleQuery
import com.lagunalabs.`swapi-graphql`.GetPersonQuery

class PeopleApiService {

    private val networking by lazy { ApolloNetworking() }

    suspend fun getPeople(): List<GetPeopleQuery.Person?>? =
        networking.fetch(GetPeopleQuery()).allPeople?.people

    suspend fun getPerson(personId: String?): GetPersonQuery.Person? =
        networking.fetch(GetPersonQuery(Optional.presentIfNotNull(personId))).person
}