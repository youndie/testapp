package ru.wip.testapp.feature.points.ui.chart

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MASK
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP
import android.view.MotionEvent.ACTION_UP
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.util.isEmpty
import com.google.android.material.color.MaterialColors
import kotlin.math.max
import kotlin.math.min


class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val contentRect = RectF()
    private val renderPath = Path()

    private var scaleFactor: Float = 1f
    private val lineWidth by lazy {
        dpToPx(1f, resources.displayMetrics)
    }

    private var dragging: Boolean = false

    private var scrollStart = PointF()
    private var zoomStart = PointF()
    private val activePointers = SparseArray<PointF>()
    private var translate = PointF()
    private var firstDraw = true

    private val backgroundColor by lazy {
        MaterialColors.getColor(this@LineChartView, com.google.android.material.R.attr.colorSurfaceContainer)
    }

    private val renderPaint = Paint().apply {
        strokeWidth = lineWidth
        style = Paint.Style.STROKE
        color = MaterialColors.getColor(this@LineChartView, com.google.android.material.R.attr.colorPrimary)
        isAntiAlias = true
    }

    private var adapter: LineChartAdapter? = null

    private val dataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            populatePath()
        }
    }

    private val scaleDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                dragging = false
                scaleFactor *= detector.scaleFactor
                scaleFactor = max(MIN_ZOOM, min(scaleFactor, MAX_ZOOM))
                zoomStart = PointF(detector.focusX, detector.focusY)
                populatePath()
                return true
            }
        })

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val x: Float = event.x
        val y: Float = event.y
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)

        when (event.action and ACTION_MASK) {
            ACTION_DOWN -> {
                dragging = true
                scrollStart = PointF(x, y)
            }

            ACTION_MOVE -> {
                val dx: Float = x - scrollStart.x
                val dy: Float = y - scrollStart.y

                if (dragging && activePointers.isEmpty()) {
                    translate = PointF(translate.x + dx / scaleFactor, translate.y + dy / scaleFactor)

                    populatePath()

                    scrollStart = PointF(x, y)
                }
            }

            ACTION_POINTER_DOWN -> {
                activePointers.put(pointerId, PointF(x, y))
            }

            ACTION_POINTER_UP -> {
                activePointers.remove(pointerId)
            }

            ACTION_UP -> {
                activePointers.clear()
                dragging = false
            }
        }

        scaleDetector.onTouchEvent(event)

        return true
    }

    fun setAdapter(adapter: LineChartAdapter) {
        this.adapter?.unregisterDataSetObserver(dataSetObserver)
        this.adapter = adapter.apply { registerDataSetObserver(dataSetObserver) }
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        updateContentRect()
        populatePath()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateContentRect()
        populatePath()
    }

    private fun updateContentRect() {
        contentRect.set(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (width - paddingEnd).toFloat(),
            (height - paddingBottom).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (firstDraw) {
            translate = PointF(-(width / 2f) + paddingStart, -(height / 2f) + paddingTop)
            firstDraw = false
            populatePath()
            return
        }

        canvas.drawColor(backgroundColor)
        canvas.drawPath(renderPath, renderPaint)
    }

    private fun populatePath() {
        adapter?.let { adapter ->
            val scaleHelper = ScaleHelper(adapter, contentRect, lineWidth, translate, scaleFactor)
            renderPath.reset()

            for (i in 0 until adapter.count - 1) {
                val x1 = scaleHelper.getX(adapter.getX(i))
                val y1 = scaleHelper.getY(adapter.getY(i))
                val x2 = scaleHelper.getX(adapter.getX(i + 1))
                val y2 = scaleHelper.getY(adapter.getY(i + 1))

                renderPath.moveTo(x1, y1)
                renderPath.cubicTo((x1 + x2) / 2f, y1, (x1 + x2) / 2f, y2, x2, y2)
            }

            invalidate()
        }
    }

    companion object {
        private const val MIN_ZOOM: Float = 0.1f
        private const val MAX_ZOOM: Float = 10f
    }
}

