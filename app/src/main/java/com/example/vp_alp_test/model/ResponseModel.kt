package com.example.vp_alp_test.model

data class ResponseModel<T>(
    val status: String,
    val data: T
)