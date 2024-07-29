package ru.wip.testapp.feature.points.ui.chart

import android.graphics.PointF
import android.graphics.RectF

class ScaleHelper(
    adapter: LineChartAdapter,
    contentRect: RectF,
    lineWidth: Float,
    private val translate: PointF,
    private val scaleFactor: Float
) {
    private val width: Float
    private val height: Float
    private val size: Int

    private val xScale: Float
    private val yScale: Float
    private val xOffset: Float
    private val yOffset: Float

    init {
        val leftPadding = contentRect.left
        val topPadding = contentRect.top

        this.width = contentRect.width() - lineWidth
        this.height = contentRect.height() - lineWidth

        this.size = adapter.count

        val bounds = adapter.dataBounds

        bounds.inset((if (bounds.width() == 0f) -1 else 0).toFloat(), (if (bounds.height() == 0f) -1 else 0).toFloat())

        val minX = bounds.left
        val maxX = bounds.right
        val minY = bounds.top
        val maxY = bounds.bottom

        this.xScale = width / (maxX - minX)
        this.xOffset = leftPadding - minX * xScale + lineWidth / 2
        this.yScale = height / (maxY - minY)
        this.yOffset = minY * yScale + topPadding + lineWidth / 2
    }

    fun getX(rawX: Float): Float {
        return (rawX * xScale + xOffset + translate.x) * scaleFactor + (width / 2f)
    }


    fun getY(rawY: Float): Float {
        return (height - rawY * yScale + yOffset + translate.y) * scaleFactor + (height / 2f)
    }
}