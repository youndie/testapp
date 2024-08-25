package ru.wip.testapp.feature.points.data

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.wip.testapp.feature.points.domain.ShareService
import java.io.IOException


class ShareServiceImpl(
  private val context: Context,
  private val dispatcher: CoroutineDispatcher
) : ShareService {

  override suspend fun shareImage(bitmap: ByteArray, name: String) {
    context.shareAsPng(bitmap, name)
  }

  private suspend fun Context.shareAsPng(bitmap: ByteArray, displayName: String) {
    val now = System.currentTimeMillis() / 1000
    val values = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
      put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
      put(MediaStore.MediaColumns.DATE_ADDED, now)
      put(MediaStore.MediaColumns.DATE_MODIFIED, now)
    }

    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
      try {
        withContext(dispatcher) {
          contentResolver.openOutputStream(uri).use { os ->
            os?.write(bitmap)
          }
        }

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("image/png")
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)
      } catch (e: IOException) {
        contentResolver.delete(uri, null, null)
        return
      }
    }
  }
}

