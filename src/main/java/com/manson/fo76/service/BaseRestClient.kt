package com.manson.fo76.service

import com.fasterxml.jackson.databind.ObjectMapper
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider

open class BaseRestClient constructor(protected val objectMapper: ObjectMapper) {

    protected val client: Client

    init {
        val config = ClientConfig()
        config.register(JacksonJsonProvider(objectMapper))
        this.client = ClientBuilder.newClient(config)
    }
}