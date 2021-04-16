package org.abubaker.kidscanvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * UI vs Views
 * UI - Allows to perform actions
 * Views - Allow to drawn upon them
 */
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // We want to be able to draw on it
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null

    //
    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {

        // Draw Paint
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE // It will be a simple line
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        // Canvas
        mCanvasPaint = Paint(Paint.DITHER_FLAG)

        // Brush Size
        mBrushSize = 20.toFloat()

    }

    // onSizeChanged() - It exists in the view class, and our DrawingView is based on it
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        // Used Bitmap for the Canvas
        canvas = Canvas(mCanvasBitmap!!)
    }

    /**
     * What should happen when we draw
     * Note: Change Canvas to Canvas? if fails
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Start from 0,0 axis and draw on mCanvasBitmap
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        if (!mDrawPath!!.isEmpty) {

            // Set Brush size
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness

            // Set Color of custom path
            mDrawPaint!!.color = mDrawPath!!.color

            // Draw our path
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    // For internal user only
    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }

    //


}

