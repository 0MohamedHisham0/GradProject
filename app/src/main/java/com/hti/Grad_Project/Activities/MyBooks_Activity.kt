package com.hti.Grad_Project.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.hti.Grad_Project.Adapter.book_adapter
import com.hti.Grad_Project.Model.book_Model
import com.hti.Grad_Project.Model.book_page_Model
import com.hti.Grad_Project.R
import kotlinx.android.synthetic.main.activity_my_saved_book.*

class MyBooks_Activity : BaseActivity() {
    private var booksMap: Map<String, Any> = HashMap()
    private var myBooks = mutableListOf<book_Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_saved_book)

        getUsersBooks()
        bt_back_SavedBook.setOnClickListener {
            startActivity(Intent(this, Pages_NewBook_TextRec_Activity::class.java))
            finishAffinity()
        }
    }

    private fun initRv() {
        rv_pdf_SavedBook.layoutManager = GridLayoutManager(this, 2)
        rv_pdf_SavedBook.adapter = book_adapter(myBooks, this)
    }

    private fun getUsersBooks() {
        mAuth?.currentUser?.let {
            mDatabaseFireStore?.collection("UsersBooks")?.document("UsersBooks")?.collection(it.uid)
                ?.get()
                ?.addOnSuccessListener { result ->
                    for (i in result) {
                        val pages = mutableListOf<book_page_Model>()

                        booksMap = i.data

                        for (s in booksMap) {
                            val pageModel: book_page_Model =
                                book_page_Model(s.value.toString(), s.key)

                            pages.add(pageModel)
                        }

                        val model: book_Model = book_Model(i.id, pages)
                        myBooks.add(model)
                    }

                    initRv()


                }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}