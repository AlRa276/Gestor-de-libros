package com.marianaalra.booklog.domain.model


data class Book(
    val title: String,
    val fileFormat: String,
    val progress: Float,
    val status: String
)