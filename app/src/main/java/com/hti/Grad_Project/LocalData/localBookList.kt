package com.hti.Grad_Project.LocalData

import com.hti.Grad_Project.Model.LocalBookModel
import com.hti.Grad_Project.Model.Pdf_Model
import com.hti.Grad_Project.R

object localBookList {
    val bookList = listOf(
        Pdf_Model(
            R.drawable.icon_norse,"Neil Gaiman",
            134,
            "Norse Mythoiogy",
            "http://f918-45-240-191-241.ngrok.io/media/1642500882.373537/Norse_Mythology_-_Neil_Gaiman.pdf"
        ),
        Pdf_Model(
            R.drawable.image_egypt_myth,"Geraldine Pinch",
            138,
            "Hand Book of Egypt",
            "http://f918-45-240-191-241.ngrok.io/media/1642514083.485481/Handbook_of_Egyptian_Mythology.pdf"
        ),
        Pdf_Model(
            R.drawable.death_on_the_night,"Agatha Christie",
            139,
            "Death On The Nile",
            "http://f918-45-240-191-241.ngrok.io/media/1642514626.266405/Death_On_The_Nile.pdf"
        ),


    )
}