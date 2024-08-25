package ru.wip.testapp.data

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.*

class FileDataSource(
  private val context: Context,
  private val dispatcher: CoroutineDispatcher
) : DataSource {


  override suspend fun persist(tag: String, serialized: String) {
    withContext(dispatcher) {
      val file = File(context.filesDir, tag)
      val fileWriter = FileWriter(file)
      val bufferedWriter = BufferedWriter(fileWriter)
      bufferedWriter.write(serialized)
      bufferedWriter.close()
    }
  }

  override suspend fun fetch(tag: String): String {
    return withContext(dispatcher) {
      val file = File(context.filesDir, tag)
      val fileReader = FileReader(file)
      val bufferedReader = BufferedReader(fileReader)
      val stringBuilder = StringBuilder()
      var line = bufferedReader.readLine()
      while (line != null) {
        stringBuilder.append(line).append("\n")
        line = bufferedReader.readLine()
      }
      bufferedReader.close()
      stringBuilder.toString()
    }
  }
}