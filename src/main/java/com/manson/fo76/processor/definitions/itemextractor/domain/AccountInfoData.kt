package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AccountInfoData {
    var name: String = ""
}