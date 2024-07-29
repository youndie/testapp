package ru.wip.testapp.data

import android.content.Context
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileDataSource(
    private val context: Context
) : DataSource {

    override fun persist(tag: String, serialized: String) {
        val file = File(context.filesDir, tag)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(serialized)
        bufferedWriter.close()
    }

    override fun fetch(tag: String): String {
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
        return stringBuilder.toString()
    }
}