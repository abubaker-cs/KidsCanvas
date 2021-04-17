package org.abubaker.kidscanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.abubaker.kidscanvas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Binding Object
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Set Default Brush Size to 20dp
        // Brush Size // Here the default or we can initial brush/ stroke size is defined.
        binding.drawingView.setSizeForBrush(20.toFloat())
    }
}