package com.example.alp.model

data class ResponseModel<T>(
    val status: String,
    val data: T
)