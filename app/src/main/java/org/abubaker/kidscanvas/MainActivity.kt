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

        // setContentView(R.layout.activity_main)
        // binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // bindingDialogBrushSize = DialogBrushSizeBinding.inflate(layoutInflater)

        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        bindingDialogBrushSize = DialogBrushSizeBinding.inflate(layoutInflater)
        brushDialog = Dialog(this)
        view = bindingDialogBrushSize.root
        brushDialog.setContentView(view)
        brushDialog.setTitle("Brush size: ")

        // Set Default Brush Size to 20dp
        // Brush Size // Here the default or we can initial brush/ stroke size is defined.
        binding.drawingView.setSizeForBrush(20F)

        binding.ibBrush.setOnClickListener {
            showBrushSizeChooserDialog()
        }

    }


    private fun showBrushSizeChooserDialog() {

        /*Also I would not use showBrushSizeChooserDialog() entirely
        and transfer these OnClickListeners to some other function
        that is just called once in onCreate(), because only brushDialog.show()
        needs to be called multiple times*/
        bindingDialogBrushSize.ibSmallBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(10F)
            brushDialog.dismiss()

        }
        bindingDialogBrushSize.ibMediumBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(20F)
            brushDialog.dismiss()

        }
        bindingDialogBrushSize.ibLargeBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(30F)
            brushDialog.dismiss()

        }
        brushDialog.show()

        // dialogs = DataBindingUtil.setContentView(this, R.layout.dialog_brush_size)

        // val brushDialog = Dialog(this)

        // brushDialog.setContentView(R.layout.dialog_brush_size)

        // brushDialog.setTitle("Brush size: ")


        // val smallBtn = dialogs.ibSmallBrush

        // smallBtn.setOnClickListener {
        //     binding.drawingView.setSizeForBrush(10.toFloat())
        //     brushDialog.dismiss()
        // }

        // brushDialog.show()

    }
}