package com.manson.fo76.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.service.graphql.DummyGraphQLResponse
import com.manson.fo76.service.graphql.LatestVersion
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType

class UpdateService(objectMapper: ObjectMapper) : BaseRestClient(objectMapper) {

    companion object {
        const val TOKEN_1 = "804b7dc76a58bf533"
        const val TOKEN_2 = "abe98983e12587484063e94"
    }

    fun getLatestVersion(): LatestVersion {
//        TODO: setup regular graphql client instead of this
        val webResource: WebTarget = client
                .target("https://api.github.com/graphql")
        val query = "{repository(owner:\"sdaskaliesku\",name:\"modCompanionApp\"){packages(first:1){nodes{latestVersion{version}}}}}"
        val map = mapOf("query" to query)
        val response = webResource.request().header("Authorization", "bearer $TOKEN_1$TOKEN_2").accept(MediaType.APPLICATION_JSON).buildPost(Entity.entity(map, MediaType.APPLICATION_JSON)).invoke()
        return response.readEntity(DummyGraphQLResponse::class.java).data.repository.packages.nodes[0].latestVersion
    }


}