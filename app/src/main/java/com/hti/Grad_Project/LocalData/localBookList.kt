package com.hti.Grad_Project.LocalData

import com.hti.Grad_Project.Model.LocalBookModel
import com.hti.Grad_Project.R

object localBookList {
    val bookList = listOf(
        LocalBookModel(
            R.drawable.image_ai_book,
            "Stuart J. Russell",
            "Artificial Intelligence: A Modern Approach",
            "200+"
        ),
        LocalBookModel(R.drawable.icon_food_book, "Samin Nosrat", "Salt Fat Acid Heat", "100+"),

        LocalBookModel(R.drawable.icon_cloud_book, "Gavin Pretor-Pinney", "A Cloud A Day", "50+"),
    )
}