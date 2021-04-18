package org.abubaker.kidscanvas

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.abubaker.kidscanvas.databinding.ActivityMainBinding
import org.abubaker.kidscanvas.databinding.DialogBrushSizeBinding

class MainActivity : AppCompatActivity() {

    // Binding Object
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDialogBrushSize: DialogBrushSizeBinding
    private lateinit var brushDialog: Dialog

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
}