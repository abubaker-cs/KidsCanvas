package org.abubaker.kidscanvas

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import org.abubaker.kidscanvas.databinding.ActivityMainBinding
import org.abubaker.kidscanvas.databinding.DialogBrushSizeBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.pallet_pressed
            )
        )


        /**
         * Button: Brush
         */
        binding.ibBrush.setOnClickListener {
            showBrushSizeChooserDialog()
        }


        /**
         * Check for permission
         */
        binding.ibGallery.setOnClickListener {

            // Check if we already have the permission
            if (isReadStorageAllowed()) {
                // run our code to get the image from the gallery
                val pickPhotoIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                startActivityForResult(pickPhotoIntent, GALLERY)

            } else {

                // ask for the permission
                requestStoragePermission()

            }
        }


        /**
         * Button: Undo
         */
        binding.ibUndo.setOnClickListener {
            binding.drawingView.onClickUndo()
        }


        /**
         * Button: Save
         */
        binding.ibSave.setOnClickListener {
            if (isReadStorageAllowed()) {
                // We will pass directly the FrameLayout as it contains both View and Image
                BitmapAsyncTask(getBitmapFromView(binding.flDrawingViewContainer)).execute()
            } else {
                // Request permission to have access to the storage
                requestStoragePermission()
            }
        }

    }

    /**
     * ----------------------------------------------------------------------- Result: Gallery
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                try {

                    // This data has a property "data" which we will check if it is null or not
                    // if not null, then user has selected something
                    // URI: data.data has the record of the image's URI
                    if (data!!.data != null) {
                        binding.ivBackground.visibility = View.VISIBLE
                        binding.ivBackground.setImageURI(data.data)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error in parsing the image or its corrupted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace() // errors will appear in the log
                }
            }
        }
    }

    /**
     * ----------------------------------------------------------------------- Brush Dialog
     */

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
            binding.drawingView.setColor(colorTag)

            // Pressed State:
            // Swap the backgrounds for last active and currently active image button.
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed))

            // Normal State
            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallet_normal
                )
            )

            // Update View: Current view is updated with selected view in the form of ImageButton.
            mImageButtonCurrentPaint = view
        }
    }

    /**
     * ----------------------------------------------------------------------- Storage Permission
     */

    private fun requestStoragePermission() {

        // Check if permission is required
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).toString()
            )
        ) {
            Toast.makeText(this, "You need permission to add a Background", Toast.LENGTH_SHORT)
                .show()
        }

        // Ask for permission (permission code is required when asking for permission)
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), STORAGE_PERMISSION_CODE
        )

    }

    // What to do when permission is granted
    // get resultCode and compare it to the Permission Code
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {

            // If user allowed or denied
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@MainActivity,
                    "Permission granted now you can read the storage files",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Oops you just denied the permission",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }


    // Helper function
    // Now we can check if the permission was granted
    private fun isReadStorageAllowed(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // return true of true, else return false
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun getBitmapFromView(view: View): Bitmap {

        // Take the dimensions, ARGB_8888 = Defines # of colors and opacity
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        // prepare the canvas
        val canvas = Canvas(returnedBitmap)

        // Background image which we are using
        val bgDrawable = view.background

        //
        if (bgDrawable != null) {
            // if there is background image then we draw it onto the canvas
            bgDrawable.draw(canvas)
        } else {
            // if there is no background then we draw white color onto the canvas
            canvas.drawColor(Color.WHITE)
        }

        // final touch
        // Draw canvas onto the view
        view.draw(canvas)

        // return final image (bitmap)
        return returnedBitmap

    }

    /**
     * ----------------------------------------------------------------------- AsyncTask
     */
    private inner class BitmapAsyncTask(val mBitmap: Bitmap) : AsyncTask<Any, Void, String>() {

        private lateinit var mProgressDialog: Dialog

        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog()
        }

        override fun doInBackground(vararg params: Any?): String {

            var result = ""

            if (mBitmap != null) {
                try {

                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                    // Retrieve and save file from the stream
                    val f =
                        File(
                            externalCacheDir!!.absoluteFile.toString()
                                    + File.separator + "KidsDrawingApp_"
                                    + System.currentTimeMillis() / 1000 + ".png"
                        )

                    val fos = FileOutputStream(f)
                    fos.write(bytes.toByteArray())
                    fos.close()
                    result = f.absolutePath

                } catch (e: Exception) {
                    result = ""
                    e.printStackTrace()
                }
            }

            return result

        }

        // It will inform the user that everything has been done
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            cancelProgressDialog()

            if (!result!!.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "File saved successfully :$result",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Something went wrong while saving the file.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            /**
             * Share the file
             */
            MediaScannerConnection.scanFile(this@MainActivity, arrayOf(result), null) { path, uri ->

                val shareIntent = Intent()

                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)

                shareIntent.type = "image/png"

                startActivity(
                    Intent.createChooser(
                        shareIntent, "Share"
                    )
                )

            }

        }

        /**
         * Progress Dialog: Actions
         */

        private fun showProgressDialog() {
            mProgressDialog = Dialog(this@MainActivity)
            mProgressDialog.setContentView(R.layout.dialog_custom_progress) // inflate .xml file
            mProgressDialog.show()
        }

        private fun cancelProgressDialog() {
            mProgressDialog.dismiss()
        }

    }

    /**
     * -----------------------------------------------------------------------
     */

    // Companion Object to be used with permissions
    // It will store static / constant variables
    companion object {
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }

}