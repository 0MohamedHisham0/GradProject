package com.hti.Grad_Project.Utilities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.hti.Grad_Project.Model.*
import com.hti.Grad_Project.Network.Remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.thread

class MainViewModel : ViewModel() {
    var answerListModel = MutableLiveData<List<Answer_Model>>()
    var bookList = MutableLiveData<List<Pdf_Model>>()
    var questionsSaveList = MutableLiveData<List<Answer_Model>>()

    //Enhanced Google
    var enhancedGoogleAnswer = MutableLiveData<List<Answer_Model>>()
    var enhancedGoogleAnswerState = MutableLiveData("offline")

    var answerListModelState = MutableLiveData(String())
    var bookListState = MutableLiveData("offline")
    var questionsSaveListState = MutableLiveData("offline")

    fun getAnswerFromView(questionModel: QuestionAsk_Model, context: Context) {
        viewModelScope.launch {

            answerListModel = getAnswer(questionModel, context, answerListModelState)

        }
    }

    fun getUserBooks(context: Context) {

        viewModelScope.launch {
            bookList = getBookList(context = context, bookListState)
        }
    }

    fun getQuestionSaveFromViews(context: Context) {
        viewModelScope.launch {
            questionsSaveList = getQuestionsList(context = context, questionsSaveListState)
        }
    }

    fun addNewQuestionFromView(context: Context, model: Answer_Model) {
        viewModelScope.launch {
            addNewQuestionToSave(model, context)
        }
    }

    fun deleteQuestionFromView(context: Context, model: Answer_Model) {
        viewModelScope.launch {
            deleteQuestionFromSave(model, context)
        }
    }

    //Enhanced Google
    fun getAnswersEnhancedFromView(context: Context, question: String) {
        viewModelScope.launch {
            enhancedGoogleAnswer = getAnswerEnhanced(question, context, enhancedGoogleAnswerState)
        }
    }
}

private fun getAnswer(
    questionModel: QuestionAsk_Model,
    context: Context,
    answerListModelState: MutableLiveData<String>,
): MutableLiveData<List<Answer_Model>> {

    val answerClearList = MutableLiveData<List<Answer_Model>>()

    RetrofitClient.getInstance().getQuestionAnswer(
        questionModel.question,
        questionModel.prediction.toString(),
        questionModel.model,
        questionModel.folder)
        .enqueue(object : Callback<AnswerList_Model?> {
            override fun onResponse(

                call: Call<AnswerList_Model?>,
                response: Response<AnswerList_Model?>
            ) {
                if (response.isSuccessful && response.code() == 200) {

                    answerClearList.value = response.body()?.result
                    answerListModelState.value = "Succ"
                } else {
                    Toast.makeText(
                        context,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    answerListModelState.value = "Failed: ${response.message()}"

                }
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
                answerListModelState.value = "Failed: ${t.message}"

            }

        })
    return answerClearList
}

fun getBookList(
    context: Context,
    bookListState: MutableLiveData<String>
): MutableLiveData<List<Pdf_Model>> {
    var pdf: Map<String, Any> = HashMap()
    val pdfList = mutableListOf<Pdf_Model>()
    val pdfListLiveData = MutableLiveData<List<Pdf_Model>>()

    Constants.GetFireStoneDb()?.collection("UsersBooks")!!
        .document("UsersPdf")
        .collection(Constants.GetAuth()?.currentUser?.uid.toString())
        .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                bookListState.value = "succ"
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
                bookListState.value = "Failed" + task.exception?.message.toString()
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

fun addNewQuestionToSave(model: Answer_Model, context: Context) {
    FirebaseFirestore.getInstance().collection("UsersQuestionHistory")
        .document("UsersQuestionHistory")
        .collection(Constants.GetAuth()?.currentUser?.uid.toString()).document(model.query)
        .set(model).addOnCompleteListener {
            if (!it.isSuccessful) {
                Toast.makeText(context, "Failed to save question in Save", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Answer saved successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
}

fun deleteQuestionFromSave(model: Answer_Model, context: Context) {
    Constants.GetFireStoneDb()?.collection("UsersQuestionHistory")!!
        .document("UsersQuestionHistory")
        .collection(Constants.GetAuth()?.currentUser?.uid.toString()).document(model.title).delete()
}

fun getQuestionsList(
    context: Context,
    questionsSaveListState: MutableLiveData<String>
): MutableLiveData<List<Answer_Model>> {
    var question: Map<String, Any> = HashMap()

    val questionsList = mutableListOf<Answer_Model>()
    val questionsListLiveData = MutableLiveData<List<Answer_Model>>()

    Constants.GetFireStoneDb()?.collection("UsersQuestionHistory")!!
        .document("UsersQuestionHistory")
        .collection(Constants.GetAuth()?.currentUser?.uid.toString()).get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                questionsSaveListState.value = "succ"
                for (document in task.result) {
                    question = document.data
                    val model: Answer_Model = Answer_Model()

                    model.query = question["query"].toString()
                    model.answer = question["answer"].toString()
                    model.title = question["title"].toString()
                    model.accuracy = question["accuracy"].toString()
                    model.paragraph = question["paragraph"].toString()

                    questionsList.add(model)

                }
                questionsListLiveData.postValue(questionsList)

            } else {
                questionsSaveListState.value = "Failed" + task.exception?.message.toString()
                Toast.makeText(
                    context,
                    "" + task.exception!!.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    return questionsListLiveData
}

private fun getAnswerEnhanced(
    question: String,
    context: Context,
    enhancedGoogleAnswerState: MutableLiveData<String>
): MutableLiveData<List<Answer_Model>> {

    val answerEnhanced = MutableLiveData<List<Answer_Model>>()

    RetrofitClient.getInstance().enhancedGoogle(question)
        .enqueue(object : Callback<AnswerList_Model> {
            override fun onResponse(
                call: Call<AnswerList_Model>,
                response: Response<AnswerList_Model>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val model: AnswerList_Model? = response.body()
                    answerEnhanced.postValue(model?.result)
                    enhancedGoogleAnswerState.value = "succ"
                } else
                    Toast.makeText(
                        context,
                        "Failed : ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()

                enhancedGoogleAnswerState.value = response.message()

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
                enhancedGoogleAnswerState.value = t.message.toString()

                Log.i("TAG", "onFailure: ${t.message}")
            }

        })
    return answerEnhanced;
}
