package org.abubaker.kidscanvas

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import org.abubaker.kidscanvas.databinding.ActivityMainBinding
import org.abubaker.kidscanvas.databinding.DialogBrushSizeBinding

class MainActivity : AppCompatActivity() {

    // Binding Object
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDialogBrushSize: DialogBrushSizeBinding
    private lateinit var brushDialog: Dialog

    // A variable for current color is picked from color pallet.
    private var mImageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /**
         * Inflate XML: activity_main
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)


        /**
         * Inflate XML: dialog_brush_size
         */
        bindingDialogBrushSize = DialogBrushSizeBinding.inflate(layoutInflater)
        view = bindingDialogBrushSize.root
        brushDialog = Dialog(this)
        brushDialog.setContentView(view)


        /**
         * Dialog: Define Title
         */
        brushDialog.setTitle("Brush size: ")


        /**
         * Default size for the Brush/Stroke: 20
         */
        binding.drawingView.setSizeForBrush(20F)

        //
        mImageButtonCurrentPaint = binding.llPaintColors[1] as ImageButton

        // Select and make the image visible
        mImageButtonCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed))


        /**
         * Click Listener: Brush Icon
         */
        binding.ibBrush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

    }


    private fun showBrushSizeChooserDialog() {

        // Size: Small 10
        bindingDialogBrushSize.ibSmallBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(10F)
            brushDialog.dismiss()
        }

        // Size: Medium 20
        bindingDialogBrushSize.ibMediumBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(20F)
            brushDialog.dismiss()
        }

        // Size: Large 30
        bindingDialogBrushSize.ibLargeBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(30F)
            brushDialog.dismiss()
        }

        // Apply changes with new configuration
        brushDialog.show()

    }

    /**
     * Method is called when color is clicked from pallet_normal.
     *
     * @param view ImageButton on which click took place.
     */
    fun paintClicked(view: View) {
        if (view !== mImageButtonCurrentPaint) {
            // Update the color
            val imageButton = view as ImageButton
            // Here the tag is used for swaping the current color with previous color.
            // The tag stores the selected view
            val colorTag = imageButton.tag.toString()
            // The color is set as per the selected tag here.

            // binding.drawingView.color(colorTag)
            // drawing_view.setColor(colorTag)
            // Swap the backgrounds for last active and currently active image button.
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed))
            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallet_normal
                )
            )

            //Current view is updated with selected view in the form of ImageButton.
            mImageButtonCurrentPaint = view
        }
    }
}