package com.hti.Grad_Project.Activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hti.Grad_Project.Adapter.page_adapter
import com.hti.Grad_Project.Model.Pdf_Model
import com.hti.Grad_Project.Model.book_Model
import com.hti.Grad_Project.Model.book_page_Model
import com.hti.Grad_Project.Network.Remote.RetrofitClient
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.Constants
import com.hti.Grad_Project.Utilities.passUriToActivity
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_text_recognation.*
import kotlinx.android.synthetic.main.book_name_dialog.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class OCR_Activity : BaseActivity(), passUriToActivity {

    private var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 654
    private var pdfList = mutableListOf<book_page_Model>()

    private var booksMap = mutableMapOf<String, String>()
    private var oldBookOrNew = "NEW"
    private var bookName: String = ""
    private lateinit var fromHome: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognation)

        //Come from Old book
        val i = intent?.getSerializableExtra("bookPages") as? (book_Model)
        fromHome = intent?.getStringExtra("fromHome").toString()

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

        if (fromHome == "True") {
            bt_SavePDFTOFirebase_OCR.visibility = View.VISIBLE
        }


        //Ask for permission of camera upon first launch of application
        askPermissionInFirstTime()

        initRv()

        //Clicks Handling
        bt_SavePDFTOFirebase_OCR.setOnClickListener {
            openDialogSaveBook("Save")

        }

        bt_back_OCR.setOnClickListener {
            onBackPressed()
        }

        bt_AskWithoutSaving_OCR.setOnClickListener {
            openDialogSaveBook("Ask")
        }

    }

    //Recyclerview
    private fun initRv() {
        if (oldBookOrNew == "NEW")
            addPageToRv("")
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

    private fun openDialogSaveBook(buttonName: String) {
        val dialog = Dialog(this) // Context, this, etc.
        dialog.setContentView(R.layout.book_name_dialog)

        dialog.bt_save_BookNameDialog.text = buttonName

        if (bookName != "") {
            dialog.et_bookName_BookNameDialog.setText(bookName)
        }

        dialog.bt_save_BookNameDialog.setOnClickListener {
            val doc = Document()
            val mfilepath =
                "/data/user/0/com.hti.myapplication/files/docsFromAW/" + dialog.et_bookName_BookNameDialog.text.toString() + ".pdf"
            val mfilepathDis =
                "/data/user/0/com.hti.myapplication/files/" + dialog.et_bookName_BookNameDialog.text.toString() + ".pdf"

            Log.i("TAG", "openDialogSaveBook: $mfilepath")
            Log.i("TAG", "openDialogSaveBook: $mfilepathDis")

            PdfWriter.getInstance(
                doc,
                FileOutputStream(mfilepath)
            )
            doc.open()
            doc.add(Paragraph(getTextFromBook(pdfList)))
            doc.close()

            Constants.copy(File(mfilepath), File(mfilepathDis))
            savePdfToApiAndFB(path = mfilepathDis, context = applicationContext)


            dialog.dismiss()

        }
        dialog.setTitle("BookName")
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun savePdfToApiAndFB(path: String, context: Context) {

        if (path.isNotEmpty()) {
            val file = File(path)
            RetrofitClient.getInstance().postNewBook(file, file.name)
                .enqueue(object : Callback<Pdf_Model?> {
                    override fun onResponse(
                        call: Call<Pdf_Model?>,
                        response: Response<Pdf_Model?>
                    ) {
                        if (response.isSuccessful && response.code() == 201) {

                            val model: Pdf_Model? = response.body()
                            if (fromHome == "True") {
                                savePdfToFB(model!!)
                            }

                        } else
                            Toast.makeText(
                                context,
                                "Failed : ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                    }

                    override fun onFailure(
                        call: Call<Pdf_Model?>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            context,
                            "Failed ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.i("TAG", "onFailure: ${t.message}")
                    }

                })
        }


    }

    fun savePdfToFB(model: Pdf_Model) {
        Constants.GetFireStoneDb()?.collection("UsersBooks")!!
            .document("UsersPdf")
            .collection(Constants.GetAuth()?.currentUser?.uid.toString())
            .document(urlToTimeStamp(model.file)).set(model)
            .addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    "Successfully upload Pdf",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getTextFromBook(list: List<book_page_Model>): String {
        var pdfText = ""
        for (i in list) {
            pdfText += i.mainText
        }

        return pdfText
    }
}

