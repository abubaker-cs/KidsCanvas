package org.abubaker.kidscanvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * This class contains the attributes for the main layout of
 * our application.
 */

/**
 * The constructor for ViewForDrawing
 * This constructor calls the setupDrawing()
 * method. This constructor is called only
 * once when the application layout is first
 * created upon launch.
 *
 * @param context
 * @param attrs
 */

/**
 * UI vs Views
 * UI - Allows to perform actions
 * Views - Allow to drawn upon them
 */
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // We want to be able to draw on it

    // An variable of CustomPath inner class to use it further.
    private var mDrawPath: CustomPath? = null

    // An instance of the Bitmap.
    private var mCanvasBitmap: Bitmap? = null

    // The Paint class holds the style and color information about how to draw geometries, text and bitmaps.
    private var mDrawPaint: Paint? = null

    // Instance of canvas paint view.
    private var mCanvasPaint: Paint? = null

    // A variable for stroke/brush size to draw on the canvas.
    private var mBrushSize: Float = 0.toFloat()

    // A variable to hold a color of the stroke.
    private var color = Color.BLACK

    // We want the lines to stay on the screen
    private var mPaths = ArrayList<CustomPath>()

    /**
     * A variable for canvas which will be initialized later and used.
     *
     *The Canvas class holds the "draw" calls. To draw something, you need 4 basic components: A Bitmap to hold the pixels, a Canvas to host
     * the draw calls (writing into the bitmap), a drawing primitive (e.g. Rect,
     * Path, text, Bitmap), and a paint (to describe the colors and styles for the
     * drawing)
     */
    private var canvas: Canvas? = null

    //
    init {
        setUpDrawing()
    }

    /**
     * This method initializes the attributes of the
     * ViewForDrawing class.
     */
    private fun setUpDrawing() {

        // Draw Paint
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE // This is to draw a STROKE style
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND // This is for store join
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND // This is for stroke Cap

        // Canvas
        // Paint flag that enables dithering when blitting.
        mCanvasPaint = Paint(Paint.DITHER_FLAG)

        // Brush Size // Here the default or we can initial brush/ stroke size is defined.
        mBrushSize = 20.toFloat()

    }

    // onSizeChanged() - It exists in the view class, and our DrawingView is based on it
    override fun onSizeChanged(w: Int, h: Int, wprev: Int, hprev: Int) {
        super.onSizeChanged(w, h, wprev, hprev)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!) // Used Bitmap for the Canvas
    }

    /**
     * What should happen when we draw
     * Note: Change Canvas to Canvas? if fails
     */
    /**
     * This method is called when a stroke is drawn on the canvas
     * as a part of the painting.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Start from 0,0 axis and draw on mCanvasBitmap
        /**
         * Draw the specified bitmap, with its top/left corner at (x,y), using the specified paint,
         * transformed by the current matrix.
         *
         *If the bitmap and canvas have different densities, this function will take care of
         * automatically scaling the bitmap to draw at the same density as the canvas.
         *
         * @param bitmap The bitmap to be drawn
         * @param left The position of the left side of the bitmap being drawn
         * @param top The position of the top side of the bitmap being drawn
         * @param paint The paint used to draw the bitmap (may be null)
         */
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        /**
         * It will preserve lines on the screen
         */
        for (path in mPaths) {
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        /**
         *
         */
        if (!mDrawPath!!.isEmpty) {

            // Set Brush size (how thick the PAINT should be)
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness

            // Set Color of custom path
            mDrawPaint!!.color = mDrawPath!!.color

            // Draw our path
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    /**
     * This method acts as an event listener when a touch
     * event is detected on the device.
     */
    /**
     * This method acts as an event listener when a touch
     * event is detected on the device.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x // Touch event of X coordinate
        val touchY = event.y // touch event of Y coordinate

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset() // Clear any lines and curves from the path, making it empty.
                mDrawPath!!.moveTo(
                    touchX,
                    touchY
                ) // Set the beginning of the next contour to the point (x,y).
            }

            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(
                    touchX,
                    touchY
                ) // Add a line from the last point to the specified point (x,y).
            }

            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }

        invalidate()
        return true
    }

    // For internal user only
    // An inner class for custom path with two params as color and stroke size.
    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path()


}

