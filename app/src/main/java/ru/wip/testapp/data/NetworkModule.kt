package ru.wip.testapp.data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val networkModule = module {
  single<OkHttpClient> {
    val logging = HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.NONE
    }

    OkHttpClient.Builder().addInterceptor(logging).build()
  }

  single<Json> { Json }
  single<HttpClient> {
    HttpClient(OkHttp) {
      install(ContentNegotiation) {
        json(get())
      }
      engine {
        config {
          followRedirects(true)
        }
        preconfigured = get<OkHttpClient>()
      }

      defaultRequest {
        url {
          protocol = URLProtocol.HTTPS
          host = "hr-challenge.dev.tapyou.com/api/test"
        }
      }
    }
  }
}
