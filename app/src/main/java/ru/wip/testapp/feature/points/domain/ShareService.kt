package ru.wip.testapp.feature.points.domain

interface ShareService {
  suspend fun shareImage(bitmap: ByteArray, name: String)
}