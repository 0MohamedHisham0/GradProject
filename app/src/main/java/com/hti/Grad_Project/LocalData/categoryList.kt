package com.hti.Grad_Project.LocalData

import com.hti.Grad_Project.Model.CategoryModel
import com.hti.Grad_Project.Model.LocalBookModel
import com.hti.Grad_Project.Model.book_Model
import com.hti.Grad_Project.Model.book_page_Model
import com.hti.Grad_Project.R

object categoryList {
    //Pages
    val FoodBook1 = listOf(
        book_page_Model("Main Text 1", "1"),
        book_page_Model("Main Text 2", "2"),
        book_page_Model("Main Text 3", "3"),
        book_page_Model("Main Text 4", "4"),
        book_page_Model("Main Text 5", "5"),
    )

    //Book
    val FoodCategoryList = listOf(
        book_Model("Food", FoodBook1)
    )

    //Category
    val categoryList = listOf(
        CategoryModel("One Last Stop",FoodCategoryList),
        CategoryModel("Kitchen",FoodCategoryList),
        CategoryModel("Small Pleasures",FoodCategoryList),
        CategoryModel("Butter Honey Pig Bread",FoodCategoryList),
        CategoryModel("Inseparable: A Never-Before-Published Novel",FoodCategoryList),


    )
}