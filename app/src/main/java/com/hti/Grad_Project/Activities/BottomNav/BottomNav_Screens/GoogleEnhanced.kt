package com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.hti.Grad_Project.Activities.BottomNavContainerScreen
import com.hti.Grad_Project.Activities.Loading
import com.hti.Grad_Project.Model.AnswerList_Model
import com.hti.Grad_Project.Model.Answer_Model
import com.hti.Grad_Project.Model.Answer_Model_Enhanced
import com.hti.Grad_Project.Network.Remote.RetrofitClient
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.MainViewModel
import com.hti.Grad_Project.animations.ShimmerEnhancedGoogle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

val answerEnhanced = MutableLiveData<List<Answer_Model>>()
val answerEnhancedState = MutableLiveData(String())

@ExperimentalMaterialApi
@Composable
fun GoogleEnhanced() {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        GoogleEnhScreen(context)

    }
}

@ExperimentalMaterialApi
@Composable
fun GetAnswerFromEnhancedGoogle() {
    val context = LocalContext.current

    val a by answerEnhanced.observeAsState(initial = emptyList())
    val state by answerEnhancedState.observeAsState(initial = String)
    if (state == "inProgress") {
        Spacer(modifier = Modifier.height(20.dp))
        ShimmerEnhancedGoogle()
    } else if (state == "Done") {
        if (a.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
            ) {
                items(a) { answer ->
                    Answer_Model_Enhanced_Item(
                        context = context,
                        model = answer
                    )
                }

            }
        }
    }

}

@Composable
fun Answer_Model_Enhanced_Item(context: Context, model: Answer_Model) {

    //TextSpeech
    val textToSpeech: TextToSpeech = TextToSpeech(context) {}
    textToSpeech.language = Locale.UK
    val iconColor = remember { mutableStateOf(R.color.LightGray) }

    Column {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = model.answer, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = model.paragraph, fontSize = 17.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Accuracy", fontSize = 17.sp)

                Text(text = model.accuracy, fontSize = 17.sp)
            }


        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.icon_speaker),
            contentDescription = "Speaker",
            tint = colorResource(iconColor.value),
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    try {
                        textToSpeech.speak(
                            "Your Questions is :" +
                                    model.query.toString(),
                            TextToSpeech.QUEUE_FLUSH,
                            null
                        )
                        textToSpeech.speak(
                            "The Answer is :"
                                    + model.answer,
                            TextToSpeech.QUEUE_ADD,
                            null
                        )
                        textToSpeech.speak(
                            "The accuracy of Answer is :"
                                    + model.accuracy,
                            TextToSpeech.QUEUE_ADD,
                            null
                        )


                        iconColor.value = R.color.orange_main

                    } catch (e: ActivityNotFoundException) {
                        // Handling error when the service is not available.
                        e.printStackTrace()
                        Toast
                            .makeText(
                                context,
                                "Your device does not support STT.",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                }
                .size(40.dp)
                .padding(start = 0.dp)
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun GoogleEnhScreen(
    context: Context
) {

    var textFieldState_Question by remember {
        mutableStateOf("")
    }
    var submitButton by remember { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@rememberLauncherForActivityResult
            }

            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val result =
                    it.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                // Ensure result array is not null or empty to avoid errors.
                if (!result.isNullOrEmpty()) {
                    // Recognized text is in the first position.
                    val recognizedText = result[0]
                    textFieldState_Question = "$recognizedText ?"

                }

            }
        }
    Spacer(modifier = Modifier.height(20.dp))
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Your Question",
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        TextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            value = textFieldState_Question,
            placeholder = { Text(text = "Ask your Question Here!") },
            onValueChange = { textFieldState_Question = it },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.orange_main)
            ),
            trailingIcon = {

                IconButton(
                    modifier = Modifier
                        .size(25.dp),
                    onClick = {
                        // Get the Intent action
                        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                        // Language model defines the purpose, there are special models for other use cases, like search.
                        sttIntent.putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        // Adding an extra language, you can use any language from the Locale class.
                        sttIntent.putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE,
                            Locale.getDefault()
                        )
                        // Text that shows up on the Speech input prompt.
                        sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
                        try {
                            // Start the intent for a result, and pass in our request code.
                            launcher.launch(sttIntent)
                        } catch (e: ActivityNotFoundException) {
                            // Handling error when the service is not available.
                            e.printStackTrace()
                            Toast.makeText(
                                context,
                                "Your device does not support STT.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_mic),
                        contentDescription = "Mic",
                        tint = colorResource(id = R.color.orange_main)
                    )
                }


            }
        )

        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                if (answerEnhancedState.value != "inProgress") {
                    submitButton = true

                    if (textFieldState_Question == "") {
                        Toast.makeText(
                            context,
                            "You didn't enter any questions yet.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        getAnswers(textFieldState_Question, context)
                    }
                } else {
                    Toast.makeText(context, "Wait until the process finish.", Toast.LENGTH_SHORT)
                        .show()
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange_main))
        ) {
            Text(text = "ASK", color = Color.White)
        }
        if (submitButton)
            GetAnswerFromEnhancedGoogle()

    }

}

fun getAnswers(
    question: String,
    context: Context,
) {
    answerEnhancedState.value = "inProgress"
    RetrofitClient.getInstance().enhancedGoogle(question)
        .enqueue(object : Callback<AnswerList_Model> {
            override fun onResponse(
                call: Call<AnswerList_Model>,
                response: Response<AnswerList_Model>
            ) {
                answerEnhancedState.value = "Done"

                if (response.isSuccessful && response.code() == 200) {
                    val model: AnswerList_Model? = response.body()
                    answerEnhanced.postValue(model?.result)
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

                Log.i("TAG", "onFailure: ${t.message}")
            }

        })
}
