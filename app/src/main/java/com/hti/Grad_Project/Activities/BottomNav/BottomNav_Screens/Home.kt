package com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import com.google.firebase.database.DataSnapshot
import com.hti.Grad_Project.Activities.Auth.LoginActivity
import com.hti.Grad_Project.Activities.OCR_Activity
import com.hti.Grad_Project.Activities.QuestionActivity
import com.hti.Grad_Project.Activities.snackBarDemo
import com.hti.Grad_Project.LocalData.drawerList
import com.hti.Grad_Project.LocalData.localBookList
import com.hti.Grad_Project.Model.LocalBookModel
import com.hti.Grad_Project.Model.Pdf_Model
import com.hti.Grad_Project.Model.UserModel
import com.hti.Grad_Project.Network.Remote.RetrofitClient
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.Constants
import com.hti.Grad_Project.Utilities.FileUtils
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URLEncoder

@ExperimentalMaterialApi
@Composable
fun Home() {
    val context = LocalContext.current
    if (Constants.checkInternetConnection(context))
        CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
            HomeScreen()
        }
    else
        snackBarDemo()

}

@Composable
@ExperimentalMaterialApi
@Preview
fun Preview() {
    HomeScreen()
}

@ExperimentalMaterialApi
@Composable
fun HomeScreen() {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,

        content = {
            Body(
                onMenuClicked = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onOcrClicked = {
                    val i = Intent(context, OCR_Activity::class.java)
                    i.putExtra("fromHome", "True")
                    context.startActivity(i)
                },
                onUploadPdfClicked = {

                }
            )
        },
        drawerContent = {
            DrawerHome(context = context)
        },

        )
}

@ExperimentalMaterialApi
@Composable
fun Body(onMenuClicked: () -> Unit, onOcrClicked: () -> Unit, onUploadPdfClicked: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var textFieldState by remember {
        mutableStateOf("")
    }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 0.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


            Card(
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.padding(16.dp, top = 20.dp, bottom = 20.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_menu_drawer),
                    contentDescription = "Icon Menu",

                    Modifier
                        .clickable(onClick = onMenuClicked)
                        .height(50.dp)
                        .width(50.dp)
                        .padding(0.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

            }

        }

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {

            Divider(color = Color.DarkGray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(13.dp))

            Text(text = "Our Books", color = MaterialTheme.colors.onPrimary, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(13.dp))

            Divider(color = Color.DarkGray, thickness = 1.dp)

            LazyListLocalBook(context)

            Spacer(modifier = Modifier.height(13.dp))

            //Or Continue With
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(20.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Divider(
                        color = Color.LightGray,
                        thickness = 3.dp,
                        modifier = Modifier.width(20.dp)
                    )

                }

                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Or Continue with",
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Divider(
                        color = Color.LightGray,
                        thickness = 3.dp,
                        modifier = Modifier.width(20.dp)
                    )

                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 75.dp, start = 75.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(0.dp)

                ) {
                    Image(
                        painterResource(R.drawable.ic_pdf),
                        contentDescription = "Upload PDf",
                        Modifier
                            .clickable(
                                onClick = {
                                    coroutineScope.launch {
                                        setShowDialog(true)
                                    }
                                }
                            )
                            .clip(RoundedCornerShape(20.dp))
                    )
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier

                        .padding(0.dp)

                ) {
                    Image(
                        painterResource(R.drawable.ic_ocr),
                        contentDescription = "OCR",
                        Modifier
                            .clickable(
                                onClick =
                                onOcrClicked
                            )
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                    )

                }


            }
        }

        DialogAddingNewPDF(showDialog, setShowDialog, context)

    }
}

@Composable
fun DrawerHome(context: Context) {
    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        var userName by remember { mutableStateOf("") }
        var userEmail by remember { mutableStateOf("") }
        Constants.GetRef().child("Users").child(Constants.GetAuth().currentUser!!.uid).get()
            .addOnSuccessListener { dataSnapshot: DataSnapshot ->
                val userModel = dataSnapshot.getValue(UserModel::class.java)

                userName = userModel!!.userName
                userEmail = userModel.email

            }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = userName,
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 20.dp)
        )

        Text(
            text = userEmail,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 20.dp), color = Color.White
        )

        Spacer(modifier = Modifier.height(30.dp))

        for (i in drawerList.drawerList) {
            DrawerItem(i.itemName, i.iconDrawer, context)
        }
    }
}

@Composable
fun DrawerItem(itemName: String, icon: Int, context: Context) {
    Row(
        modifier = Modifier
            .padding(all = 10.dp)
            .height(50.dp)
            .fillMaxWidth()
            .clickable {
                if (itemName == "Logout") {
                    Constants
                        .GetAuth()
                        ?.signOut()
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    Toast
                        .makeText(context, "Successfully Logout", Toast.LENGTH_SHORT)
                        .show()
                }

            }
    ) {
        Image(
            painterResource(id = icon), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(5.dp)
                .height(40.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(corner = CornerSize(10.dp)))
        )

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            Text(
                text = itemName,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@ExperimentalMaterialApi
@Composable
fun LazyListLocalBook(context: Context) {

    val list = remember { localBookList.bookList }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 10.dp)
    ) {
        items(list) { book ->
            ItemBookList(book = book, context = context)
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun ItemBookList(book: Pdf_Model, context: Context) {
    Card(
        modifier = Modifier
            .padding(all = 6.dp)
            .width(160.dp)
            .height(270.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        onClick = {
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra("pdfModel", book)
            context.startActivity(intent)
        }

    ) {
        Column {
            Column(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = book.pdfIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(5.dp)
                        .height(210.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(corner = CornerSize(10.dp)))
                )

                Spacer(modifier = Modifier.height(7.dp))

                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = book.title, maxLines = 2)

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = book.author,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )

                }

            }

        }
    }
}

object RippleCustomTheme : RippleTheme {

    //Your custom implementation...
    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            Color.LightGray,
            lightTheme = true
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            Color.LightGray,
            lightTheme = true
        )
}

@Composable
fun DialogAddingNewPDF(showDialog: Boolean, setShowDialog: (Boolean) -> Unit, context: Context) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            onDismissRequest = {
                Toast.makeText(context, "You Didn't save any pdf!", Toast.LENGTH_SHORT).show()
            },
            confirmButton = {

            },
            dismissButton = {

            },
            text = {
                var bookName by remember { mutableStateOf("Upload Pdf") }
                var Uri: Uri = Uri.EMPTY

                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                        if (uri != null) {
                            Uri = uri
                            val documentFile = DocumentFile.fromSingleUri(context, uri)
                            bookName = documentFile?.name.toString()

                            if (!FileUtils.isExternalStorageDocument(uri))
                                Log.i(
                                    "TAG", "DialogAddingNewPDF: ${
                                        FileUtils.copyFileToInternalStorage(
                                            uri,
                                            "docsFromAW",
                                            context
                                        )
                                    }"
                                )
                            else
                                Log.i(
                                    "TAG",
                                    "DialogAddingNewPDF: ${FileUtils.getPath(uri, context)}"
                                )

                        }

                    }

                fun selectDocument() {
                    try {
                        launcher.launch(("application/pdf"))
                    } catch (ex: ActivityNotFoundException) {
                        Log.e(ContentValues.TAG, "Couldn't start an activity to pick a document")
                    }

                }

                fun isLetters(string: String): Boolean {
                    return string.none {
                        it !in 'A'..'Z' && it !in 'a'..'z' && it !in '0'..'9' && it !in '0'..'9' && it !in charArrayOf(
                            '.',
                            '$',
                            '!',
                            '+',
                            '-',
                            '_',
                            ' ',
                            '(',
                            ')'
                        )
                    }
                }

                fun urlToTimeStamp(link: String): String {
                    val idStr: String = link.substring(link.lastIndexOf("/media/") + 7)
                    val last = idStr.substring(idStr.lastIndexOf("/"))
                    val done = idStr.replace(last, "")
                    return done
                }

                fun savePdfToFB(model: Pdf_Model) {
                    model.title = model.title.toString().replace("\"", "")
                    Constants.GetFireStoneDb()?.collection("UsersBooks")!!
                        .document("UsersPdf")
                        .collection(Constants.GetAuth()?.currentUser?.uid.toString())
                        .document(model.id.toString()).set(model)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Successfully upload Pdf",
                                Toast.LENGTH_SHORT
                            ).show()

                            setShowDialog(false)

                        }
                }

                fun savePdfToApiAndFB(uri: Uri) {
                    if (Uri != android.net.Uri.EMPTY && bookName != "Upload Pdf") {

                        Toast.makeText(
                            context,
                            "We are uploading you pdf, Please wait",
                            Toast.LENGTH_LONG
                        ).show()
                        val file: File = if (!FileUtils.isExternalStorageDocument(uri)) {
                            val copedToEx = FileUtils.copyFileToInternalStorage(
                                uri,
                                "docsFromAW",
                                context
                            )
                            File(copedToEx)
                        } else {
                            File(FileUtils.getPath(uri, context))
                        }

                        RetrofitClient.getInstance().postNewBook(file, bookName)
                            .enqueue(object : Callback<Pdf_Model?> {
                                override fun onResponse(
                                    call: Call<Pdf_Model?>,
                                    response: Response<Pdf_Model?>
                                ) {
                                    if (response.isSuccessful && response.code() == 201) {

                                        val model: Pdf_Model? = response.body()
                                        savePdfToFB(model!!)

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

                    if (bookName == "Upload Pdf" || Uri == android.net.Uri.EMPTY) {
                        Toast.makeText(context, "Browse you pdf to be uploaded", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                Column() {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Adding New Pdf")

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            selectDocument()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                1.dp,
                                color = colorResource(id = R.color.orange_main),
                                shape = RoundedCornerShape(30)
                            )
                            .padding(start = 0.dp, end = 0.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pdf),
                            contentDescription = "Icon"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(bookName, maxLines = 2)
                        Spacer(modifier = Modifier.width(10.dp))

                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {


                        Button(
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange_main)),
                            onClick = {
                                // Change the state to close the dialog
                                setShowDialog(false)
                            },
                        ) {
                            Text("Dismiss")
                        }
                        Spacer(modifier = Modifier.width(20.dp))

                        Button(
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange_main)),
                            onClick = {
                                savePdfToApiAndFB(Uri)

                            },
                        ) {
                            Text("Save")
                        }
                    }


                }

            })
    }

}

