package com.hti.Grad_Project.Activities

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hti.Grad_Project.ui.theme.GradProjectTheme
import java.io.File


class PdfViwerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GradProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PdfViewerScreen()
                }
            }
        }
    }

    companion object {
        private const val PDF_SELECTION_CODE = 99
    }
}

@Composable
fun PdfViewerScreen() {


    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri: Uri? ->

            if (uri != null) {
                val file = File(uri.path)

            }
        }


    fun selectDocument() {
        try {
            launcher.launch(arrayOf("application/pdf"))
        } catch (ex: ActivityNotFoundException) {
            Log.e(TAG, "Couldn't start an activity to pick a document")
        }

    }

    Column(Modifier.fillMaxSize()) {
        Button(
            onClick = { selectDocument() },
            Modifier
                .width(300.dp)
                .height(100.dp)
        ) {
            Text(text = "Load PDF")
        }

    }


}


fun Context.getActivity(): AppCompatActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

fun getFilename(contentResolver: ContentResolver, uri: Uri): String? {
    return when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.getString(nameIndex);
            }
        }
        ContentResolver.SCHEME_FILE -> {
            uri.path?.let { path ->
                File(path).name
            }
        }
        else -> null
    }
}