package com.manson.fo76.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.service.graphql.DummyGraphQLResponse
import com.manson.fo76.service.graphql.LatestVersion
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType

class UpdateService(objectMapper: ObjectMapper) : BaseRestClient(objectMapper) {

    companion object {
        const val TOKEN = "671446c1fd8f2e2736073ca04d02cd401ceb20a0"
    }

    fun getLatestVersion(): LatestVersion {
//        TODO: setup regular graphql client instead of this
        val webResource: WebTarget = client
                .target("https://api.github.com/graphql")
        val query = "{repository(owner:\"sdaskaliesku\",name:\"modCompanionApp\"){packages(first:1){nodes{latestVersion{version}}}}}"
        val map = mapOf("query" to query)
        val response = webResource.request().header("Authorization", "bearer $TOKEN").accept(MediaType.APPLICATION_JSON).buildPost(Entity.entity(map, MediaType.APPLICATION_JSON)).invoke()
        return response.readEntity(DummyGraphQLResponse::class.java).data.repository.packages.nodes[0].latestVersion
    }


}