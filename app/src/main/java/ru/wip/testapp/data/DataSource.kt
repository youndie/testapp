package ru.wip.testapp.data

interface DataSource {
    fun persist(tag: String, serialized: String)
    fun fetch(tag: String): String
}