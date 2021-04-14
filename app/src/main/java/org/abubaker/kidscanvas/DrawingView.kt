package org.abubaker.kidscanvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
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


    // For internal user only
    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {

    }

    //


}

