package ru.wip.testapp.feature.points.domain

import ru.wip.testapp.domain.Result

interface PointsRepository {
  suspend fun load(count: Int): Result<List<Point>>
  suspend fun fetch(): List<Point>
}
