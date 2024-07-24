package com.example.calculator_apiclient.classes.data

import kotlinx.serialization.Serializable

@Serializable
data class RESTOperation(
    var method: String,
    var name: String,
    var path: String,
)
