package com.hti.Grad_Project.LocalData

import com.hti.Grad_Project.R

object drawerList {
    val drawerList = listOf(
        drawerModel(R.drawable.icon_edit_profile, "Change your email and password"),
        drawerModel(R.drawable.icon_ocr, "Convert your image to pdf"),
        drawerModel(R.drawable.icon_logout, "Logout"),
    )
}

data class drawerModel(
    val iconDrawer: Int,
    val itemName: String
)