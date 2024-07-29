package ru.wip.testapp.feature.main.domain

data class MainUiState(
    val number: Int? = null,
    val loading: Boolean = false,
    val error: String? = null
)