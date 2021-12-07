package com.hti.Grad_Project.Activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hti.Grad_Project.Model.book_page_Model
import com.hti.Grad_Project.R
import kotlinx.android.synthetic.main.activity_text_recognation.*

import java.io.IOException
import androidx.recyclerview.widget.GridLayoutManager
import com.hti.Grad_Project.Adapter.page_adapter
import com.hti.Grad_Project.Model.book_Model
import com.hti.Grad_Project.Utilities.passUriToActivity
import kotlinx.android.synthetic.main.book_name_dialog.*


class Pages_NewBook_TextRec_Activity : BaseActivity(), passUriToActivity {

    private var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private var pdfList = mutableListOf<book_page_Model>()

    private var booksMap = mutableMapOf<String, String>()
    private var oldBookOrNew = "NEW"
    private var bookName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognation)


        //Came from Old book

        var i = intent?.getSerializableExtra("bookPages") as? (book_Model)

        if (i != null) {

            if (i.pagesList != null) {
                pdfList = i.pagesList
                bookName = i.bookName
                tv_toolbar_OCR.text = i.bookName
            }

            if (i.bookName != null && i.pagesList.size != 0) {
                oldBookOrNew = "OLD"
            }

        }

        //if User is Authenticated
        if (!mAuth?.currentUser?.uid?.isEmpty()!!) {
            bt_SavePDFTOFirebase_OCR.visibility = View.VISIBLE
        }

        //Ask for permission of camera upon first launch of application
        askPermissionInFirstTime()

        initRv()

        //Clicks Handling
        bt_SavePDFTOFirebase_OCR.setOnClickListener {
            openDialogSaveBook()
        }

        bt_back_OCR.setOnClickListener {
            startActivity(Intent(this, MyBooks_Activity::class.java))
            finishAffinity()
        }

    }

    //Add Data To Store Firebase
    private fun addBookToFireBase(bookName: String, list: MutableList<book_page_Model>) {
        for (i in list) {
            booksMap[i.pageNum] = i.mainText
            list[0].mainText = bookName
            mDatabaseFireStore?.collection("UsersBooks")!!
                .document("UsersBooks")
                .collection(mAuth?.currentUser?.uid.toString()).document(bookName).set(booksMap)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Your Book Saved Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }

    //Recyclerview
    private fun initRv() {
        if (oldBookOrNew == "NEW")
            addPageToRv("FirstPage")
        rv_pdf_OCR.layoutManager = GridLayoutManager(this, 2)
        rv_pdf_OCR.adapter =
            page_adapter(this, pdfList, this, bookName)
    }

    private fun addPageToRv(mainText: String) {
        var p: book_page_Model =
            book_page_Model(
                mainText,
                (pdfList.size).toString()
            )
        p.mainText = mainText
        p.pageNum = (pdfList.size).toString()
        pdfList.add(p)
        rv_pdf_OCR.adapter =
            page_adapter(this, pdfList, this, bookName)
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

    private fun openDialogSaveBook() {
        val dialog = Dialog(this) // Context, this, etc.
        dialog.setContentView(R.layout.book_name_dialog)

        if (bookName != "") {
            dialog.et_bookName_BookNameDialog.setText(bookName)
        }

        dialog.bt_save_BookNameDialog.setOnClickListener {
            addBookToFireBase(dialog.et_bookName_BookNameDialog.text.toString(), pdfList)
            dialog.dismiss()
        }
        dialog.setTitle("BookName")
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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

    //Unused Fun

    //BrowseImage From Phone
    private fun openGalleryToPickImage() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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

}






