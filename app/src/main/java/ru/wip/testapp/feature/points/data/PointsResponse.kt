package ru.wip.testapp.feature.points.data

import kotlinx.serialization.Serializable
import ru.wip.testapp.feature.points.domain.Point

@Serializable
class PointsResponse(val points: List<Point>)