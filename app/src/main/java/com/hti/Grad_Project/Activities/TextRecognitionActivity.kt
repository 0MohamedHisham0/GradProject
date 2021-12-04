package com.hti.Grad_Project.Activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hti.Grad_Project.Adapter.ocr_pdf_adapter
import com.hti.Grad_Project.Model.pdf_Model
import com.hti.myapplication.R
import kotlinx.android.synthetic.main.activity_text_recognation.*

import java.io.FileDescriptor
import java.io.IOException
import androidx.recyclerview.widget.GridLayoutManager
import com.hti.Grad_Project.Utilities.passUriToActivity


class TextRecognitionActivity : BaseActivity(), passUriToActivity {

    private var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private val pdfList = mutableListOf<pdf_Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognation)

        //Ask for permission of camera upon first launch of application
        askPermissionInFirstTime()

        initRv()

    }


    //Recyclerview
    private fun initRv() {
        addPageToRv("FirstPage")
        rv_pdf_OCR.layoutManager = GridLayoutManager(this, 2)
        rv_pdf_OCR.adapter = ocr_pdf_adapter(this, pdfList, this)
    }

    private fun addPageToRv(mainText: String) {
        var p: pdf_Model = pdf_Model(mainText, (pdfList.size).toString())
        p.mainText = mainText
        p.pageNum = (pdfList.size).toString()
        pdfList.add(p)
        rv_pdf_OCR.adapter = ocr_pdf_adapter(this, pdfList, this)
    }

    //TextRecognition
    private fun recognizer(imageUri: Uri) {
        try {
            val image = InputImage.fromFilePath(this, imageUri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Task completed successfully
                    // ...
                    addPageToRv(visionText.text)
                    Toast.makeText(this, "successfully Recognize your image!", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...

                    Toast.makeText(
                        this,
                        "Something want wrong please try again!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    //Permissions
    private fun askPermissionInFirstTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 112)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //For Camera Capture
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {

            //Recognizing the image Text
            recognizer(image_uri!!)

        }

        //For Gallery Images

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            image_uri = data.data

            //Recognizing the image Text
            recognizer(image_uri!!)


        }
    }

    override fun onCaptureImage(value: Uri?) {
        image_uri = value
    }


    //Unused Fun

    //BrowseImage From Phone
    private fun openGalleryToPickImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
    }

    private fun validateCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )


                requestPermissions(permission, 121)
            } else {
                openCamera()

            }
        }
    }

    //Opens camera to capture image
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }
}






