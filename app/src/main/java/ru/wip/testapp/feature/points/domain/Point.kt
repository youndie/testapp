package ru.wip.testapp.feature.points.domain

import kotlinx.serialization.Serializable

@Serializable
data class Point(val x: Float, val y: Float)