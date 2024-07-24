package com.example.calculator_apiclient.classes.data

import kotlinx.serialization.Serializable

@Serializable
data class OperationsResponse(var operations : ArrayList<RESTOperation>)