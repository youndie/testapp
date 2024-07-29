package ru.wip.testapp.feature.points.domain

import ru.wip.testapp.domain.Result

interface PointsRepository {
    suspend fun load(count: Int): Result<List<Point>>
    fun fetch(): List<Point>
}
