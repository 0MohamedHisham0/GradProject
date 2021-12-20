package com.hti.Grad_Project.Activities

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.hti.Grad_Project.Activities.Auth.LoginActivity
import com.hti.Grad_Project.Activities.BottomNav.NavigationController
import com.hti.Grad_Project.LocalData.drawerList
import com.hti.Grad_Project.LocalData.localBookList
import com.hti.Grad_Project.Model.LocalBookModel
import com.hti.Grad_Project.Model.UserModel
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.Constants
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun Home() {
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        HomeScreen()
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
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
                    context.startActivity(
                        Intent(
                            context,
                            Pages_NewBook_TextRec_Activity::class.java
                        )
                    )
                },
                onUploadPdfClicked = {
                    context.startActivity(
                        Intent(
                            context,
                            QuestionActivity::class.java
                        )
                    )
                })
        },
        drawerContent = {
            DrawerHome(context = context)
        },

        )
}

@ExperimentalMaterialApi
@Composable
fun Body(onMenuClicked: () -> Unit, onOcrClicked: () -> Unit, onUploadPdfClicked: () -> Unit) {

    var textFieldState by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Spacer(modifier = Modifier.padding(14.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_menu_drawer),
                contentDescription = "Icon Menu",

                Modifier
                    .clickable(onClick = onMenuClicked)
                    .height(50.dp)
                    .width(50.dp)
                    .clip(RoundedCornerShape(10.dp))
            )


        }

        Spacer(modifier = Modifier.padding(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(0.83f),
                value = textFieldState,
                placeholder = { Text(text = "Search for Category") },
                onValueChange = { textFieldState = it },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(id = R.color.orange_main)
                ),

                )
            Spacer(modifier = Modifier.width(15.dp))

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_search_bt),
                    contentDescription = "Icon Menu",

                    Modifier
                        .clickable(onClick = { TODO() })
                        .height(50.dp)
                        .width(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Our Books", color = MaterialTheme.colors.onPrimary, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(13.dp))

        Divider(color = Color.DarkGray, thickness = 1.dp)

        LazyListLocalBook {

        }

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

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 75.dp, start = 75.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Card(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(
                        onClick =
                        onUploadPdfClicked
                    )
            ) {
                Image(
                    painterResource(R.drawable.ic_pdf),
                    contentDescription = "Upload PDf"
                )
            }

            Card(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .clickable(
                        onClick =
                        onOcrClicked
                    )
                    .padding(10.dp)
            ) {
                Image(
                    painterResource(R.drawable.ic_ocr),
                    contentDescription = "OCR"
                )
            }


        }

    }
}

@Composable
fun DrawerHome(context: Context) {
    // Column Composable
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

@Preview
@Composable
fun DrawerPreview() {
    DrawerItem("Logout", R.drawable.icon_logout, TODO())

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
                if (itemName == "My Saved Books") {
                    context.startActivity(Intent(context, MyBooks_Activity::class.java))
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

@Composable
fun LazyListLocalBook(selectedItem: (LocalBookModel) -> Unit) {

    val list = remember { localBookList.bookList }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    ) {
        items(list) { book ->
            ItemBookList(book = book)
        }
    }

}

@Composable
fun ItemBookList(book: LocalBookModel) {
    Card(
        modifier = Modifier
            .padding(all = 5.dp)
            .width(300.dp)
            .height(110.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ItemBookImage(book = book)


                Spacer(modifier = Modifier.width(4.dp))

                Column {

                    Text(text = book.bookName, maxLines = 2)

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(7.dp),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom
            ) {
                Text(text = book.numAskedInBook, fontSize = 13.sp)
                Text(text = " Asked in this book", fontSize = 13.sp)
            }
        }

    }
}

@Composable
fun ItemBookImage(book: LocalBookModel) {
    Image(
        painter = painterResource(id = book.bookImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(5.dp)
            .height(40.dp)
            .width(40.dp)
            .clip(RoundedCornerShape(corner = CornerSize(10.dp)))

    )
}


object RippleCustomTheme : RippleTheme {

    //Your custom implementation...
    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            Color.Black,
            lightTheme = true
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            Color.Black,
            lightTheme = true
        )
}