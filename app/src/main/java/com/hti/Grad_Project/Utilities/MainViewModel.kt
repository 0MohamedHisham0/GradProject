package com.hti.Grad_Project.Utilities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hti.Grad_Project.Model.AnswerList_Model
import com.hti.Grad_Project.Model.Answer_Model
import com.hti.Grad_Project.Model.Pdf_Model
import com.hti.Grad_Project.Model.QuestionAsk_Model
import com.hti.Grad_Project.Network.Remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel : ViewModel() {

    var answerListModel = MutableLiveData<List<Answer_Model>>()
    var bookList = MutableLiveData<List<Pdf_Model>>()

    fun getAnswerFromView(questionModel: QuestionAsk_Model, context: Context) {
        viewModelScope.launch {
            answerListModel = getAnswer(questionModel, context)
        }
    }

    fun getUserBooks(context: Context) {
        viewModelScope.launch {
            bookList = getBookList(context = context)
        }
    }
}

private fun getAnswer(
    questionModel: QuestionAsk_Model,
    context: Context
): MutableLiveData<List<Answer_Model>> {

    val answerClearList = MutableLiveData<List<Answer_Model>>()

    RetrofitClient.getInstance().getQuestionAnswer(
        questionModel.question,
        questionModel.prediction.toString(),
        questionModel.model,
        questionModel.folder
    )
        .enqueue(object : Callback<AnswerList_Model?> {
            override fun onResponse(

                call: Call<AnswerList_Model?>,
                response: Response<AnswerList_Model?>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val model: AnswerList_Model? = response.body()

                    answerClearList.postValue(model!!.result)

                } else
                    Toast.makeText(
                        context,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
            }

            override fun onFailure(
                call: Call<AnswerList_Model?>,
                t: Throwable
            ) {
                Toast.makeText(
                    context,
                    "Failed ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()

                Log.i("TAG", "onFailure: ${t.message}")
            }

        })
    return answerClearList;
}

fun getBookList(context: Context): MutableLiveData<List<Pdf_Model>> {
    var pdf: Map<String, Any> = HashMap()
    val pdfList = mutableListOf<Pdf_Model>()
    val pdfListLiveData = MutableLiveData<List<Pdf_Model>>()

    Constants.GetFireStoneDb()?.collection("UsersBooks")!!
        .document("UsersPdf")
        .collection(Constants.GetAuth()?.currentUser?.uid.toString())
        .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    pdf = document.data

                    val model: Pdf_Model = Pdf_Model()
                    model.title = pdf.get("title").toString()
                    model.file = pdf.get("file").toString()
                    model.id = pdf.get("id").toString().toInt()

                    pdfList.add(model)

                }
                pdfListLiveData.postValue(pdfList)

            } else {
                Toast.makeText(
                    context,
                    "" + task.exception!!.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    return pdfListLiveData
}
