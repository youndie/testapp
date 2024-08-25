package ru.wip.testapp.feature.points.ui.chart

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.graphics.PointF
import android.graphics.RectF

class LineChartAdapter(
  private var data: List<PointF>
) {

  private val observable = DataSetObservable()

  val count: Int get() = data.size

  fun setData(data: List<PointF>) {
    this.data = data
    notifyDataSetChanged()
  }

  fun getY(index: Int): Float = data[index].y

  fun getX(index: Int): Float = data[index].x

  val dataBounds: RectF
    get() {
      val count = count
      var minY = Float.MAX_VALUE
      var maxY = -Float.MAX_VALUE
      var minX = Float.MAX_VALUE
      var maxX = -Float.MAX_VALUE
      for (i in 0 until count) {
        val x = getX(i)
        minX = minX.coerceAtMost(x)
        maxX = maxX.coerceAtLeast(x)
        val y = getY(i)
        minY = minY.coerceAtMost(y)
        maxY = maxY.coerceAtLeast(y)
      }

      return RectF(minX, minY, maxX, maxY)
    }

  private fun notifyDataSetChanged() {
    observable.notifyChanged()
  }

  fun registerDataSetObserver(observer: DataSetObserver) {
    observable.registerObserver(observer)
  }

  fun unregisterDataSetObserver(observer: DataSetObserver) {
    observable.unregisterObserver(observer)
  }

}