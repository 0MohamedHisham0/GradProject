package com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.hti.Grad_Project.Activities.BottomNav.BottomNavScreenDataModel

@Composable
fun GoogleEnhanced() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Google Enhanced",
            fontSize = 20.sp
        )
    }
}