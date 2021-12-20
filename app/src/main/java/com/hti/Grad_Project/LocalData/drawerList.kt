package com.hti.Grad_Project.LocalData

import com.hti.Grad_Project.Model.LocalBookModel
import com.hti.Grad_Project.R

object drawerList {
    val drawerList = listOf(
        drawerModel(R.drawable.icon_edit_profile, "Edit Profile"),
        drawerModel(R.drawable.icon_favorite, "My Favorite"),
        drawerModel(R.drawable.icon_history, "My Questions History"),
        drawerModel(R.drawable.icon_saved_book, "My Saved Books"),
        drawerModel(R.drawable.icon_logout, "Logout"),
    )
}

data class drawerModel(
    val iconDrawer: Int,
    val itemName: String
)