package com.example.android.art.glow.drawing.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val path = Path()
    private val path1 = Path()

    private val paint = Paint().apply {
        color = Color.BLACK // default color
        strokeWidth = 10f // stroke width for drawing
        style = Paint.Style.STROKE
        isAntiAlias = true // for smoother drawing
    }

    private val erasePaint = Paint().apply {
        color = Color.parseColor("#0e0e0e") // Dark gray for eraser
        strokeWidth = 10f // stroke width for drawing
        style = Paint.Style.STROKE
        isAntiAlias = true // for smoother drawing
    }


    // List of icons (bitmaps) and their positions
    private val iconList = mutableListOf<Pair<Drawable, RectF>>() // Each pair contains a Drawable and its position
    private var selectedIcon: Drawable? = null // Currently selected icon

    private var isRedo = false
    private var isUndo = false

    private var currentColor = Color.BLACK // To store the current drawing color
    private var isEraserMode = false // Flag to toggle between drawing and eraser mode

    // Stacks for Undo/Redo functionality
    val undoStack = mutableListOf<Path>()
    val undoEraseStack = mutableListOf<Path>()
    val redoStack = mutableListOf<Path>()
    val redoEraseStack = mutableListOf<Path>()

    // Callback to notify the Activity when redo stack changes
    var onRedoStackChanged: (() -> Unit)? = null
    var onUndoStackChanged: (() -> Unit)? = null

    // This function will handle the actual drawing on the canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isEraserMode) {
            canvas.drawPath(path1, erasePaint)
        } else {
            canvas.drawPath(path, paint)
        }


        // Draw all paths in the undo stack
        if(!isEraserMode) {
          for (p in undoStack) {
              canvas.drawPath(p, paint)
          }
        } else {
            for (p in undoEraseStack) {
                canvas.drawPath(p, erasePaint)
            }
        }
    }

    // This function will handle touch events for drawing
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (selectedIcon != null) {
                    println("selectedIcon: $selectedIcon")
                    // If an icon is selected, add it to the icon list at the touched position
                    val iconWidth = dpToPx(46f) // Convert dp to px for fixed width
                    val iconHeight = dpToPx(46f) // Convert dp to px for fixed height
                    val rect = RectF(x - iconWidth / 2, y - iconHeight / 2,
                        x + iconWidth / 2, y + iconHeight / 2)
                    iconList.add(Pair(selectedIcon!!, rect))
                    invalidate() // Redraw after placing the icon
                } else {
                    path.moveTo(x, y) // Start a new path at the touch point
                    path1.moveTo(x, y)
                }

            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                path1.lineTo(x,y)
            }
            MotionEvent.ACTION_UP -> {
                undoStack.add(Path(path))
                undoEraseStack.add(Path(path))

                onRedoStackChanged?.invoke()
                onUndoStackChanged?.invoke()
                path.reset()
                path1.reset()
            }
        }

        invalidate() // Redraw the view
        return true // Indicate the event was handled
    }

    // Functions to set the color dynamically
    fun setColor(color: Int) {
        currentColor = color
        paint.color = currentColor
    }

    // Undo the last drawing action
    fun undo() {
        if (undoStack.isNotEmpty()) {
            // Move the last path to the redo stack
            val lastPath = undoStack.removeAt(undoStack.size - 1)
            onRedoStackChanged?.invoke()
            onUndoStackChanged?.invoke()
            redoStack.add(lastPath)
            invalidate() // Redraw
        } else if(undoEraseStack.isNotEmpty()) {
            // Move the last path to the redo stack
            val lastPath = undoEraseStack.removeAt(undoEraseStack.size - 1)
            onRedoStackChanged?.invoke()
            onUndoStackChanged?.invoke()
            redoEraseStack.add(lastPath)
            invalidate() // Redraw
        }
    }

    // Redo the last undone action
    fun redo() {
        if (redoStack.isNotEmpty()) {
            // Move the last path from redo stack back to undo stack
            val lastUndonePath = redoStack.removeAt(redoStack.size - 1)
            onRedoStackChanged?.invoke()
            onUndoStackChanged?.invoke()
            undoStack.add(lastUndonePath)
            invalidate() // Redraw
        }
        else if (redoEraseStack.isNotEmpty()) {
            // Move the last path from redo stack back to undo stack
            val lastUndonePath = redoEraseStack.removeAt(redoEraseStack.size - 1)
            onRedoStackChanged?.invoke()
            onUndoStackChanged?.invoke()
            redoEraseStack.add(lastUndonePath)
            invalidate() // Redraw
        }
    }

    // Check if redo stack is empty
    fun isRedoStackEmpty(): Boolean {
        return redoStack.isEmpty() && redoEraseStack.isEmpty()
    }

    // Check if redo stack is empty
    fun isUndoStackEmpty(): Boolean {
        return undoStack.isEmpty() && undoEraseStack.isEmpty()
    }

    // Toggle the eraser mode
    fun toggleEraserMode() {
        isEraserMode = !isEraserMode
    }

    // Set the selected icon
    fun setSelectedIcon(icon: Drawable) {
        selectedIcon = icon
    }

    fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }
}