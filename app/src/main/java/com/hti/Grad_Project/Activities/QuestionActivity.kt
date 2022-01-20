package com.hti.Grad_Project.Activities

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SwitchDefaults.colors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens.RippleCustomTheme
import com.hti.Grad_Project.Activities.ui.theme.ComposeBottomNavigationTheme
import com.hti.Grad_Project.Model.Pdf_Model
import com.hti.Grad_Project.Model.QuestionAsk_Model
import com.hti.Grad_Project.R
import java.util.*

@ExperimentalMaterialApi
class QuestionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ComposeBottomNavigationTheme {
                Surface(color = colors.background) {
                    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                        ScaffoldQuestionScreen(
                            onBackButtonClick = { onBackPressed() },
                            ParagraphName = "Food Book"
                        )
                    }
                }
            }

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ScaffoldQuestionScreen(
    onBackButtonClick: () -> Unit,
    ParagraphName: String
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            QuestionScreen(
                onBackButtonClick = onBackButtonClick,
                ParagraphName = ParagraphName,
                onSubmitClicked = {
                    val intent = Intent(context, AnswersActivity::class.java)
                    intent.putExtra("preNum", 2)
                    context.startActivity(intent)
                }
            )
        })
}

@ExperimentalMaterialApi
@Composable
fun QuestionScreen(
    onBackButtonClick: () -> Unit,
    onSubmitClicked: () -> Unit,
    ParagraphName: String
) {
    //Var
    val context = LocalContext.current
    val intent = (context as QuestionActivity).intent
    val pdfModel = intent.getSerializableExtra("pdfModel") as Pdf_Model


    val pdfName by remember {
        mutableStateOf(pdfModel.title)
    }
    var textFieldState_Question by remember {
        mutableStateOf("")
    }
    var textFieldState_Predictions by remember {
        mutableStateOf("")
    }
    val isCheckedDistlBert = remember { mutableStateOf(false) }
    val isCheckedBert = remember { mutableStateOf(true) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@rememberLauncherForActivityResult
            }


            if (it.resultCode == RESULT_OK && it.data != null) {
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

    Column(modifier = Modifier.padding(16.dp)) {

        //Views

        Spacer(modifier = Modifier.height(16.dp))

        TopBarQuestionScreen(
            onBackButtonClick = (onBackButtonClick),
            ParagraphName = pdfName
        )

        Spacer(modifier = Modifier.size(30.dp))

        Text(
            text = "Your Question",
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        TextField(
            modifier = Modifier
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

        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text = "Number of Predictions",
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxWidth(),
                value = textFieldState_Predictions,
                placeholder = { Text(text = "1~19") },
                onValueChange = { textFieldState_Predictions = it },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(id = R.color.orange_main)
                ),
            )

            Button(
                onClick = {
                    val model: QuestionAsk_Model
                    val intent = Intent(context, AnswersActivity::class.java)
                    if (textFieldState_Question != "" && textFieldState_Predictions != "") {
                        if (textFieldState_Predictions.toInt() in 1..19) {
                            if (isCheckedBert.value) {
                                model = QuestionAsk_Model(
                                    textFieldState_Question,
                                    "bert_qa.joblib",
                                    textFieldState_Predictions.toInt(),
                                    "media/${urlToTimeStamp(pdfModel.file)}"
                                )
                                intent.putExtra("Question", model)
                                context.startActivity(intent)

                            } else if (isCheckedDistlBert.value) {
                                model = QuestionAsk_Model(
                                    textFieldState_Question,
                                    "distilbert_qa.joblib",
                                    textFieldState_Predictions.toInt(),
                                    "media/${urlToTimeStamp(pdfModel.file)}"
                                )
                                intent.putExtra("Question", model)
                                context.startActivity(intent)

                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Predictions number should be less than 20 and greater than 0.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Enter Question and Prediction number first!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp), shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange_main))
            ) {
                Text(text = "Submit", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.size(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(text = "DistlBert", fontSize = 20.sp)
                Text(
                    text = "Smaller, faster, cheaper, lighter",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            Switch(
                checked = isCheckedDistlBert.value,
                onCheckedChange = { checked ->
                    isCheckedDistlBert.value = checked
                    isCheckedBert.value = !checked
                },
                colors = colors(
                    checkedThumbColor = colorResource(id = R.color.orange_main)
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(text = "Bert", fontSize = 20.sp)
                Text(text = "Deeper", fontSize = 13.sp, color = Color.DarkGray)
            }

            Switch(
                checked = isCheckedBert.value,
                onCheckedChange = { checked ->
                    isCheckedBert.value = checked
                    isCheckedDistlBert.value = !checked
                },
                colors = colors(
                    checkedThumbColor = colorResource(id = R.color.orange_main)
                )
            )
        }


    }
}


@Composable
fun TopBarQuestionScreen(onBackButtonClick: () -> Unit, ParagraphName: String) {
    Row {

        IconButton(
            modifier = Modifier
                .size(25.dp), onClick = onBackButtonClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = "Back Button"
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Text(text = ParagraphName, fontSize = 19.sp)
    }
}

fun urlToTimeStamp(link: String): String {
    val idStr: String = link.substring(link.lastIndexOf("/media/") + 7)
    val last = idStr.substring(idStr.lastIndexOf("/"))
    val done = idStr.replace(last, "")
    return done
}
