package com.hti.Grad_Project.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

import com.hti.Grad_Project.Activities.BottomNav.NavigationController
import com.hti.Grad_Project.Activities.ui.theme.ComposeBottomNavigationTheme

class BottomNavContainerScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBottomNavigationTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavigationController()
                }
            }
        }
    }
}
