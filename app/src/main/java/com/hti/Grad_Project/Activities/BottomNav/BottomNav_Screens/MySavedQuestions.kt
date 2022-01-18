package com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens

import android.app.Activity
import android.app.Instrumentation
import android.content.ActivityNotFoundException
import android.content.Context
import android.speech.RecognizerIntent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hti.Grad_Project.Activities.ModelBottomSheet
import com.hti.Grad_Project.Activities.selectedItem
import com.hti.Grad_Project.Activities.ui.theme.ShimmerAnimationJetPackComposeTheme
import com.hti.Grad_Project.Model.Answer_Model
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.MainViewModel
import com.hti.Grad_Project.animations.ShimmerAnimateBookItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.text.Typography.paragraph
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.hti.Grad_Project.Utilities.FileUtils
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.speech.tts.TextToSpeech.OnInitListener
import androidx.core.app.ActivityCompat
import androidx.test.core.app.ApplicationProvider.getApplicationContext


@ExperimentalMaterialApi
@Composable
fun MySavedQuestions() {
    val mainViewModel = MainViewModel()

    val context = LocalContext.current
    mainViewModel.getQuestionSaveFromViews(context)

    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            GetQuestionSaveLiveData(
                mainViewModel.questionsSaveList,
                context,
                mainViewModel.questionsSaveListState
            )


        }

    }
}

@ExperimentalMaterialApi
@Composable
fun GetQuestionSaveLiveData(
    questionLiveData: LiveData<List<Answer_Model>>,
    context: Context,
    questionsSaveListState: MutableState<String>
) {

    val questionList by questionLiveData.observeAsState(initial = emptyList())
    SaveScreen(questionList, context, questionsSaveListState)


}

@Composable
fun Error(error: String) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painterResource(id = R.drawable.vector_error), contentDescription = "errorImage")

        Spacer(modifier = Modifier.width(20.dp))

        Text(text = error, fontSize = 20.sp)
    }
}

@Composable
fun NotFound() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.vector_not_found),
            contentDescription = "errorImage",
            modifier = Modifier.size(height = 200.dp, width = 200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "You didn't saved questions yet", fontSize = 20.sp)
    }
}

@Composable
fun LoadingQuestionSave() {
    LazyColumn(Modifier.fillMaxSize()) {
        repeat(10) {
            item { ShimmerAnimateBookItem() }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SaveScreen(
    questionList: List<Answer_Model>,
    context: Context,
    questionsSaveListState: MutableState<String>
) {
    var textFieldState by remember {
        mutableStateOf("")
    }

    //Bottom Sheet
    val selectedItem = remember {
        selectedItemSaveQuestion()
    }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                value = textFieldState,
                placeholder = { Text(text = "Search for Questions") },
                onValueChange = {
                    textFieldState = it
                },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(id = R.color.orange_main)
                ),

                )
            Spacer(modifier = Modifier.width(15.dp))

        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Saved Questions",
            color = MaterialTheme.colors.onPrimary,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(13.dp))

        Divider(color = Color.DarkGray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(13.dp))

        if (questionsSaveListState.value == "offline") {
            //Pending
            ShimmerAnimationJetPackComposeTheme() {

                LoadingQuestionSave()
            }
        } else if (questionsSaveListState.value == "succ" && questionList.isNotEmpty()) {
            //Data Successfully and not empty
            QuestionSaveLazyList(
                searchedText = textFieldState,
                context = context,
                list = questionList,
                scope = scope,
                modalBottomSheetState = modalBottomSheetState, selectedItem = selectedItem
            )
        } else if (questionsSaveListState.value.contains("Failed")) {
            //Failed
            Error(error = questionsSaveListState.value)
        } else if (questionsSaveListState.value == "succ" && questionList.isEmpty()) {
            //Data is Successfully and Empty
            NotFound()
        }
    }


    ModelBottomSheetSaveQuestion(
        scope = scope, modalBottomSheetState = modalBottomSheetState, selectedItem, context
    )


}

@ExperimentalMaterialApi
@Composable
fun QuestionSaveLazyList(
    searchedText: String, list: List<Answer_Model>, scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    selectedItem: selectedItemSaveQuestion, context: Context
) {
    var filteredList: List<Answer_Model>
    LazyColumn(contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)) {

        filteredList = if (searchedText.isEmpty()) {
            list
        } else {
            val resultList = mutableListOf<Answer_Model>()
            for (pdf in list) {
                if (pdf.query.lowercase(Locale.getDefault())
                        .contains(searchedText.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(pdf)
                }
            }
            resultList
        }
        items(filteredList) { item ->
            QuestionSaveItem(model = item, scope, modalBottomSheetState, selectedItem, context)
        }

    }
}

@ExperimentalMaterialApi
@Composable
fun QuestionSaveItem(
    model: Answer_Model,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    selectedItem: selectedItemSaveQuestion, context: Context
) {


    Card(
        modifier = Modifier
            .padding(all = 5.dp)
            .fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        onClick = {
            scope.launch {

                selectedItem.query = model.query + ""
                selectedItem.paragraph = model.paragraph + ""
                selectedItem.title = model.title + ""
                selectedItem.answer = model.answer + ""
                selectedItem.aquarcy = model.accuracy + ""

                modalBottomSheetState.show()

            }
        }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 7.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = model.query, maxLines = 1, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = model.answer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 7.dp), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Accuracy: " + model.accuracy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "From: " + model.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )

                }
            }


        }

    }
}


@Composable
@ExperimentalMaterialApi
fun ModelBottomSheetSaveQuestion(
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    model: selectedItemSaveQuestion, context: Context
) {
    val iconColor = remember { mutableStateOf(R.color.LightGray) }
    val iconColor_Spaeker = remember { mutableStateOf(R.color.LightGray) }

    //TextSpeech
    val textToSpeech: TextToSpeech = TextToSpeech(context) {}
    textToSpeech.language = Locale.UK

    if (!modalBottomSheetState.isVisible) {
        iconColor.value = R.color.LightGray
        iconColor_Spaeker.value = R.color.LightGray
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        sheetState = modalBottomSheetState,
        sheetContent = {

            Column(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),

                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        //Diver
                        Divider(
                            color = Color.LightGray,
                            thickness = 3.dp,
                            modifier = Modifier
                                .width(50.dp)
                                .clip(RoundedCornerShape(13.dp))
                        )

                        Spacer(modifier = Modifier.width(120.dp))
                        Column(
                            modifier = Modifier.size(40.dp),

                            ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_speaker),
                                contentDescription = "SaveAnswer",
                                tint =  colorResource(iconColor_Spaeker.value),
                                modifier = Modifier
                                    .clickable {
                                        try {
                                            textToSpeech.speak("Your Questions is :"+
                                                model.query.toString(),
                                                TextToSpeech.QUEUE_FLUSH,
                                                null
                                            )
                                            textToSpeech.speak("The Answer is :"
                                                + model.answer,
                                                TextToSpeech.QUEUE_ADD,
                                                null
                                            )
                                            textToSpeech.speak("The accuracy of Answer is :"
                                                + model.aquarcy,
                                                TextToSpeech.QUEUE_ADD,
                                                null
                                            )


                                            iconColor_Spaeker.value = R.color.orange_main

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
                    Text(text = model.query, fontSize = 25.sp)

                    Spacer(modifier = Modifier.width(100.dp))

                    Spacer(modifier = Modifier.height(20.dp))

                    //Answer
                    Text(text = model.answer, fontSize = 25.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    //Paragraph
                    Text(text = model.paragraph, fontSize = 16.sp, color = Color.LightGray)

                    Spacer(modifier = Modifier.height(20.dp))

                    //acq
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Accuracy", fontSize = 14.sp, color = Color.LightGray,
                        )
                        Text(text = model.aquarcy, fontSize = 14.sp, color = Color.LightGray)
                    }

                }


            }

        }) {
    }

}

class selectedItemSaveQuestion : ViewModel() {
    var query: String by mutableStateOf("")
    var paragraph: String by mutableStateOf("")
    var title: String by mutableStateOf("")
    var answer: String by mutableStateOf("")
    var aquarcy: String by mutableStateOf("")


}
