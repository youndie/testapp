package ru.wip.testapp.feature.points.domain

interface ShareService {
    fun shareImage(bitmap: ByteArray, name: String)
}