package com.hti.Grad_Project.Activities.BottomNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_CATEGORY
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_HOME
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_SAVED_BOOKS
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_SETTING

sealed class BottomNavScreenDataModel(val route: String, var label: String, val icon: ImageVector) {

    object Home : BottomNavScreenDataModel(ROUTE_HOME, "Home", Icons.Default.Home)
    object Category : BottomNavScreenDataModel(ROUTE_CATEGORY, "Category", Icons.Default.List)
    object SavedBooks : BottomNavScreenDataModel(ROUTE_SAVED_BOOKS, "Saved Books", Icons.Default.Build)
    object Setting : BottomNavScreenDataModel(ROUTE_SETTING, "Setting", Icons.Default.Settings)


    object Items {
        val items = listOf(
            Home, Category, SavedBooks, Setting
        )
    }
}