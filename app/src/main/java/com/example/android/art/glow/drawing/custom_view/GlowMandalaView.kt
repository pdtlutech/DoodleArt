package com.example.android.art.glow.drawing.custom_view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.android.art.glow.drawing.R
import kotlin.math.*

enum class StrokeStyle {
    SOLID, DASHED, DOTTED
}

class GlowMandalaView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var paint = Paint()
    private var currentPaint = Paint(paint)

    private var centerX = 0f
    private var centerY = 0f
    private var lastX = 0f
    private var lastY = 0f
    private var isEraserMode = false
    var previousColor = 0
    private var symmetryCount = 8

    private val currentPaths = mutableListOf<Path>()
    private val undoStack = mutableListOf<DrawItem>()
    private val redoStack = mutableListOf<DrawItem>()

    private var selectedDrawableResourceId = 0
    private var svgWidthPx = 0
    private var svgHeightPx = 0

    // Bitmap cache
    private lateinit var drawBitmap: Bitmap
    private lateinit var drawCanvas: Canvas

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)

        val scale = context.resources.displayMetrics.density
        svgWidthPx = (92 * scale).toInt()
        svgHeightPx = (92 * scale).toInt()
    }

    private var touchDrawListener: OnTouchDrawListener? = null

    fun setOnTouchDrawListener(listener: OnTouchDrawListener) {
        this.touchDrawListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f

        drawBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(drawBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)
        for (path in currentPaths) {
            canvas.drawPath(path, currentPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x - centerX
        val y = event.y - centerY

        // Send event back
        touchDrawListener?.onUserTouch(event.x, event.y, event.action)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dropPicture(event)
                startNewPath(x, y)
            }

            MotionEvent.ACTION_MOVE -> {
                if (selectedDrawableResourceId != 0) {
                    dropPicture(event)
                } else {
                    updatePath(x, y)
                }
            }

            MotionEvent.ACTION_UP -> {
                savePath()
            }
        }

        invalidate()
        return true
    }

    private fun dropPicture(event: MotionEvent) {
        if (selectedDrawableResourceId == 0) return

        val position = Rect(
            event.x.toInt() - svgWidthPx / 2,
            event.y.toInt() - svgWidthPx / 2,
            event.x.toInt() + svgWidthPx / 2,
            event.y.toInt() + svgWidthPx / 2,
        )

        val drawable = ContextCompat.getDrawable(context, selectedDrawableResourceId)
        drawable?.let {
            it.setBounds(0, 0, svgWidthPx, svgHeightPx)
            drawCanvas.save()
            drawCanvas.translate(position.left.toFloat(), position.top.toFloat())
            it.draw(drawCanvas)
            drawCanvas.restore()

            undoStack.add(DrawItem.SvgItem(it, position))
            redoStack.clear()
        }
    }

    private fun startNewPath(x: Float, y: Float) {
        currentPaths.clear()

        if (isEraserMode) {
            currentPaths.add(Path())
            lastX = x
            lastY = y
            currentPaths.first().moveTo(centerX + x, centerY + y)
        } else {
            repeat(symmetryCount) { currentPaths.add(Path()) }
            lastX = x
            lastY = y
            addSymmetricPaths(x, y, isMove = false)
        }
    }

    private fun updatePath(x: Float, y: Float) {
        if (abs(x - lastX) >= 4 || abs(y - lastY) >= 4) {
            if (isEraserMode) {
                val path = currentPaths.first()
                path.lineTo(centerX + x, centerY + y)

                // Commit ngay và chuẩn bị path mới
                commitCurrentPaths()
                startNewPath(x, y)
            } else {
                addSymmetricPaths(x, y, isMove = true)
            }

            lastX = x
            lastY = y
        }
    }

    private fun savePath() {
        commitCurrentPaths()
        currentPaths.clear()
    }

    private fun commitCurrentPaths() {
        for (path in currentPaths) {
            drawCanvas.drawPath(path, currentPaint)
            undoStack.add(DrawItem.PathItem(Path(path), Paint(currentPaint)))
        }
        redoStack.clear()
    }

    private fun addSymmetricPaths(x: Float, y: Float, isMove: Boolean) {
        try {
            val angleStep = 360f / symmetryCount
            for (i in 0 until symmetryCount) {
                if( i>= currentPaths.size) return

                val angleRad = Math.toRadians((angleStep * i).toDouble())
                val xRotated = (x * cos(angleRad) - y * sin(angleRad)).toFloat()
                val yRotated = (x * sin(angleRad) + y * cos(angleRad)).toFloat()
                val path = currentPaths[i]

                val drawX = centerX + xRotated
                val drawY = centerY + yRotated

                if (isMove) path.lineTo(drawX, drawY)
                else path.moveTo(drawX, drawY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setGlowColor(color: Int = Color.CYAN, size: Int = 30, isDefault: Boolean = false) {
        paint = Paint().apply {
            this.color = if (isDefault) Color.RED else color
            strokeWidth = size.toFloat()
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.SOLID)
        }
        currentPaint = Paint(paint)
    }

    fun setEraserMode(enabled: Boolean, selectedColor: Int = context.getColor(R.color.customBlack)) {
        previousColor = currentPaint.color
        isEraserMode = enabled
        val eraserPaint = Paint().apply {
            color = selectedColor
            strokeWidth = 60f
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        currentPaint = eraserPaint
    }

    fun setSelectedSvgResource(resourceId: Int) {
        selectedDrawableResourceId = resourceId
    }

    fun setSymmetry(count: Int) {
        symmetryCount = count
    }

    fun clearCanvas() {
        drawBitmap.eraseColor(Color.TRANSPARENT)
        undoStack.clear()
        redoStack.clear()
        currentPaths.clear()
        invalidate()
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val item = undoStack.removeAt(undoStack.size - 1)
            println("undoStack: $item")
            redoStack.add(item)
            redrawFromStack()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val item = redoStack.removeAt(redoStack.size -1)
            undoStack.add(item)
            redrawFromStack()
        }
    }

    private fun redrawFromStack() {
        drawBitmap.eraseColor(Color.TRANSPARENT)
        for (item in undoStack) {
            when (item) {
                is DrawItem.PathItem -> {
                    drawCanvas.drawPath(item.path, item.paint)
                }
                is DrawItem.SvgItem -> {
                    drawCanvas.save()
                    drawCanvas.translate(item.position.left.toFloat(), item.position.top.toFloat())
                    item.drawable.draw(drawCanvas)
                    drawCanvas.restore()
                }
            }
        }
        invalidate()
    }

    fun setStrokeSize(size: Float) {
        paint.strokeWidth = size
    }

    fun setStrokeStyle(style: StrokeStyle) {
        val effect = when (style) {
            StrokeStyle.SOLID -> null
            StrokeStyle.DASHED -> DashPathEffect(floatArrayOf(50f, 50f), 0f)
            StrokeStyle.DOTTED -> DashPathEffect(floatArrayOf(10f, 40f), 0f)
        }

        paint.pathEffect = effect
        currentPaint = Paint(paint)
    }

    sealed class DrawItem {
        data class PathItem(val path: Path, val paint: Paint) : DrawItem()
        data class SvgItem(val drawable: Drawable, val position: Rect) : DrawItem()
    }


    interface OnTouchDrawListener {
        fun onUserTouch(x: Float, y: Float, action: Int)
    }
}
