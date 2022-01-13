package com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.hti.Grad_Project.Activities.QuestionActivity
import com.hti.Grad_Project.Activities.snackBarDemo
import com.hti.Grad_Project.Activities.ui.theme.ShimmerAnimationJetPackComposeTheme
import com.hti.Grad_Project.Model.Pdf_Model
import com.hti.Grad_Project.Utilities.Constants
import com.hti.Grad_Project.Utilities.MainViewModel
import com.hti.Grad_Project.animations.ShimmerAnimateBookItem
import java.util.*

@ExperimentalMaterialApi
@Composable
fun MyPdf() {
    val mainViewModel = MainViewModel()

    val context = LocalContext.current
    mainViewModel.getUserBooks(context)

    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        if (Constants.checkInternetConnection(context))
            GetBookListLiveData(
                pdfListLiveData = mainViewModel.bookList,
                mainViewModel.bookListState
            )
        else
            snackBarDemo()
    }
}

@ExperimentalMaterialApi
@Composable
fun GetBookListLiveData(
    pdfListLiveData: LiveData<List<Pdf_Model>>,
    bookListState: MutableState<String>
) {
    var context = LocalContext.current
    val pdfList by pdfListLiveData.observeAsState(initial = emptyList())
    CategoryScreen(pdfList, bookListState)

}

@ExperimentalMaterialApi
@Composable
fun CategoryScreen(pdfList: List<Pdf_Model>, bookListState: MutableState<String>) {
    val context = LocalContext.current

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        scaffoldState = scaffoldState,

        content = {


            Body(onButtonSearchClicked = {}, pdfList, bookListState)
        },
        drawerContent = {
            DrawerHome(context = context)
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun Body(
    onButtonSearchClicked: () -> Unit,
    pdfList: List<Pdf_Model>,
    bookListState: MutableState<String>
): String {



    var textFieldState by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(0.83f),
                value = textFieldState,
                placeholder = { Text(text = "Search for Pdf") },
                onValueChange = {
                    textFieldState = it
                },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(id = com.hti.Grad_Project.R.color.orange_main)
                ),

                )
            Spacer(modifier = Modifier.width(15.dp))

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                Image(
                    imageVector = ImageVector.vectorResource(id = com.hti.Grad_Project.R.drawable.icon_search_bt),
                    contentDescription = "Icon Search",

                    Modifier
                        .clickable(onClick = onButtonSearchClicked)
                        .height(50.dp)
                        .width(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Your Category", color = MaterialTheme.colors.onPrimary, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(13.dp))

        Divider(color = Color.DarkGray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(13.dp))

        if (bookListState.value == "offline") {
            //Pending
            ShimmerAnimationJetPackComposeTheme() {
                LoadingBookList()
            }
        } else if (bookListState.value == "succ" && pdfList.isNotEmpty()) {
            //Data Successfully and not empty
            BookLazyList(searchedText = textFieldState, context = context, pdfList)
        } else if (bookListState.value.contains("Failed")) {
            //Failed


            Error(error = bookListState.value)
        } else if (bookListState.value == "succ" && pdfList.isEmpty()) {
            //Data is Successfully and Empty
            NotFound()
        }

        Spacer(modifier = Modifier.height(13.dp))

    }
    return textFieldState;
}

@ExperimentalMaterialApi
@Composable
fun BookListItem(pdf: Pdf_Model, context: Context) {
    Card(
        modifier = Modifier
            .padding(all = 5.dp)
            .fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        onClick = {
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra("pdfModel", pdf)
            context.startActivity(intent)
        }
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 7.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = pdf.title, maxLines = 2, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = pdf.file,
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

@Composable
fun LoadingBookList() {
    LazyColumn(Modifier.fillMaxSize()) {
        repeat(10) {
            item { ShimmerAnimateBookItem() }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BookLazyList(searchedText: String, context: Context, pdfList: List<Pdf_Model>) {
    var filteredList: List<Pdf_Model>

    LazyColumn(contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)) {

        filteredList = if (searchedText.isEmpty()) {
            pdfList
        } else {
            val resultList = mutableListOf<Pdf_Model>()
            for (pdf in pdfList) {
                if (pdf.title.lowercase(Locale.getDefault())
                        .contains(searchedText.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(pdf)
                }
            }
            resultList
        }

        items(filteredList) { item ->
            BookListItem(pdf = item, context)
        }

    }
}

