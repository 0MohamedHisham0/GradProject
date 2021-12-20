package com.hti.Grad_Project.Activities

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hti.Grad_Project.LocalData.categoryList
import com.hti.Grad_Project.Model.CategoryModel
import com.hti.Grad_Project.R
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalMaterialApi
@Composable
fun Category() {
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        CategoryScreen()
    }
}


@ExperimentalMaterialApi
@Composable
fun CategoryScreen() {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,

        content = {
            Body(
                onButtonSearchClicked = {

                    coroutineScope.launch {
                    }
                })
        },
        drawerContent = {
            DrawerHome(context = context)
        },

        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(bottom = 50.dp),
                backgroundColor = colorResource(id = com.hti.Grad_Project.R.color.orange_main),
                onClick = {
                    coroutineScope.launch {
                        setShowDialog(true)
                    }

                }) {
                DialogAddingNewCategory(showDialog, setShowDialog)
                Text(text = "+", fontSize = 23.sp)
            }
        }

    )
}

@ExperimentalMaterialApi
@Composable
fun Body(onButtonSearchClicked: () -> Unit): String {

    var textFieldState by remember {
        mutableStateOf("")
    }

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
                placeholder = { Text(text = "Search for Category") },
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

        SearchForCategory(searchedText = textFieldState)

    }
    return textFieldState;
}

@Composable
fun CategoryLazyList(selectedItem: (CategoryModel) -> Unit) {

    val list = remember { categoryList.categoryList }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
    ) {
        items(list) { category ->
            ItemCategory(category = category)
        }
    }

}

@Composable
fun ItemCategory(category: CategoryModel) {

    Card(
        modifier = Modifier
            .padding(all = 5.dp)
            .fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(15.dp))
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
                        .padding(start = 14.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = category.categoryName, maxLines = 2, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "${category.booksList.size.toString()} Book",
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
fun SearchForCategory(searchedText: String) {
    val list = remember { categoryList.categoryList }
    var filteredList: List<CategoryModel>
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
    ) {
        filteredList = if (searchedText.isEmpty()) {
            list
        } else {
            val resultList = mutableListOf<CategoryModel>()
            for (category in list) {
                if (category.categoryName.lowercase(Locale.getDefault())
                        .contains(searchedText.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(category)
                }
            }
            resultList
        }
        items(filteredList) { filteredList ->
            ItemCategory(category = filteredList)
        }
    }
}


@Composable
fun DialogAddingNewCategory(showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
    var textFieldState by remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {

            },

            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange_main)),
                    onClick = {
                        // Change the state to close the dialog
                        setShowDialog(false)
                    },
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
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
            },
            text = {
                Column() {

                    Text("Adding New Category")

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        value = textFieldState,
                        placeholder = { Text(text = "New Category Name") },
                        onValueChange = { textFieldState = it },
                        shape = RoundedCornerShape(15.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = colorResource(id = com.hti.Grad_Project.R.color.orange_main)
                        ),

                        )
                }

            },
        )
    }
}
