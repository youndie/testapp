package ru.wip.testapp.feature.points.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.wip.testapp.data.DataSource
import ru.wip.testapp.domain.NetworkError
import ru.wip.testapp.domain.Result
import ru.wip.testapp.domain.ServerError
import ru.wip.testapp.feature.points.domain.InvalidCountError
import ru.wip.testapp.feature.points.domain.Point
import ru.wip.testapp.feature.points.domain.PointsRepository

class PointsRepositoryImpl(
    private val httpClient: HttpClient,
    private val persistDataSource: DataSource,
    private val json: Json,
) : PointsRepository {

    override suspend fun load(count: Int): Result<List<Point>> {
        return try {
            val response = httpClient.get("points") {
                parameter("count", count)
            }

            when {
                (response.status == HttpStatusCode.InternalServerError) ->
                    Result<List<Point>>(null, ServerError(response.bodyAsText()))

                (ContentType.Application.Json.contentType in response.contentType()?.contentType.orEmpty()) -> {
                    val points = response.body<PointsResponse>().points
                    persist(points)

                    Result(points, null)
                }

                else -> Result<List<Point>>(null, InvalidCountError(response.bodyAsText()))
            }

        } catch (e: Exception) {
            Result(null, NetworkError(e.message.toString()))
        }
    }

    override fun fetch(): List<Point> {
        return json.decodeFromString(persistDataSource.fetch(POINTS_PERSIST_FILENAME))
    }

    private fun persist(list: List<Point>) {
        persistDataSource.persist(POINTS_PERSIST_FILENAME, json.encodeToString(list))
    }

    companion object {
        private const val POINTS_PERSIST_FILENAME = "points.json"
    }
}


