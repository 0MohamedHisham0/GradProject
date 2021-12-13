package com.example.composebottomnavigation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Home() {
    Scaffold()


}

@Preview
@Composable
fun Preview() {
    Scaffold()
}


@Composable
fun Scaffold() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopBar(onMenuClicked = {
            coroutineScope.launch {
                // to close use -> scaffoldState.drawerState.close()
                scaffoldState.drawerState.open()
            }
        })

    }) {


    }
}

@Composable
fun TopBar(onMenuClicked: () -> Unit) {
    Row() {


        Surface(
            modifier = Modifier
                .clickable(onClick = onMenuClicked)
                .clip(RoundedCornerShape(5.dp))
                .padding(20.dp)
                .size(27.dp),
            color = Color.White,
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu Btn", tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Surface(
            modifier = Modifier.background(color = Color.Blue)

        ) {

        }

    }
}