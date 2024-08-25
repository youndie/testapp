package ru.wip.testapp.data

interface DataSource {
  suspend fun persist(tag: String, serialized: String)
  suspend fun fetch(tag: String): String
}