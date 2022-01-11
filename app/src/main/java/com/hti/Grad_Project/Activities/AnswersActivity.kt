package com.hti.Grad_Project.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens.RippleCustomTheme
import com.hti.Grad_Project.Activities.ui.theme.ComposeBottomNavigationTheme
import com.hti.Grad_Project.LocalData.answerList
import com.hti.Grad_Project.Model.Answer_Model
import com.hti.Grad_Project.Model.QuestionAsk_Model
import com.hti.Grad_Project.Model.answerModel
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
class AnswersActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val intent = (context as AnswersActivity).intent
            val questionModel = intent.getSerializableExtra("Question") as QuestionAsk_Model
            mainViewModel.getAnswerFromView(questionModel, context)

            ComposeBottomNavigationTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                        getAnswerLiveData(
                            mainViewModel.answerListModel,
                            questionModel
                        )
                    }


                }

            }

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun getAnswerLiveData(
    personListLiveData: LiveData<List<Answer_Model>>,
    questionaskModel: QuestionAsk_Model
) {

    val answersList by personListLiveData.observeAsState(initial = emptyList())
    if (answersList == null) {
        Loading()
    } else {
        AnswerScreen(answersList, questionaskModel)
    }
}

@Composable
fun Loading() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            modifier = Modifier.wrapContentWidth(CenterHorizontally), color = colorResource(
                id = R.color.orange_main
            )
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun AnswerScreen(
    list: List<Answer_Model>,
    questionModel: QuestionAsk_Model
) {

    val context = LocalContext.current

    val IsBertClicked = remember { mutableStateOf(true) }

    val ColorOfBert = remember { mutableStateOf(R.color.orange_main) }
    val ColorOfDistlBert = remember { mutableStateOf(R.color.orange_main) }

    val myList = remember { mutableStateListOf<answerModel>() }
    val firstList = remember {
        answerList.answerBertList
    }
    val currentListSize = remember { mutableStateOf(questionModel.prediction) }
    val question = remember { mutableStateOf(questionModel.question) }


    //Bottom Sheet
    val selectedItem = remember {
        selectedItem()
    }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()


    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(10.dp))

        TopBarAnswerScreen(onBackButtonClick = {
            context.startActivity(
                Intent(
                    context,
                    QuestionActivity::class.java
                )
            )
        }, ParagraphName = questionModel.question)

        Spacer(modifier = Modifier.height(60.dp))

        Text(text = "${questionModel.prediction} Answers", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            enabled = false,
            onClick = {},
            modifier = Modifier
                .border(
                    1.dp,
                    color = colorResource(id = R.color.orange_main),
                    shape = RoundedCornerShape(30)
                )
                .height(50.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)

        ) {
            if (questionModel.model == "bert_qa.joblib") {
                Text(text = "Bert", color = Color.White, fontSize = 18.sp)
            } else {
                Text(text = "DistlBert", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        AnswerLazyList(
            list = list,
            scope = scope,
            modalBottomSheetState = modalBottomSheetState,
            selectedItem = selectedItem, context
        )
        currentListSize.value = list.size
    }

    ModelBottomSheet(
        scope = scope, modalBottomSheetState = modalBottomSheetState, selectedItem, context
    )

}


@Composable
@ExperimentalMaterialApi
fun ModelBottomSheet(
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    model: selectedItem, context: Context
) {
    val iconColor = remember { mutableStateOf(R.color.LightGray) }
    if (!modalBottomSheetState.isVisible)
        iconColor.value = R.color.LightGray

    ModalBottomSheetLayout(
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
                    //Diver
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
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
                                painter = painterResource(id = R.drawable.icon_save),
                                contentDescription = "SaveAnswer",
                                tint = colorResource(iconColor.value),
                                modifier = Modifier
                                    .clickable {
                                        val answerModel = Answer_Model(
                                            model.query,
                                            model.answer,
                                            model.title,
                                            model.paragraph,
                                            model.aquarcy
                                        )
                                        MainViewModel().addNewQuestionFromView(
                                            context,
                                            answerModel
                                        )
                                        iconColor.value = R.color.orange_main
                                    }
                                    .size(40.dp)
                                    .padding(start = 0.dp)
                            )
                        }

                    }


                    Spacer(modifier = Modifier.height(20.dp))

                    //Answer
                    Text(text = model.answer, fontSize = 25.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    //Paragraph
                    Text(text = model.paragraph, fontSize = 16.sp, color = LightGray)

                    Spacer(modifier = Modifier.height(20.dp))

                    //acq
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Accuracy", fontSize = 14.sp, color = LightGray,
                        )
                        Text(text = model.aquarcy, fontSize = 14.sp, color = LightGray)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(), shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.orange_main)
                        )
                    ) {
                        Text(text = "Rate this Answer", color = Color.White)
                    }

                }


            }

        }) {
    }

}

@Composable
fun TopBarAnswerScreen(onBackButtonClick: () -> Unit, ParagraphName: String) {
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

@ExperimentalMaterialApi
@Composable
fun AnswerLazyList(
    list: List<Answer_Model>,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    selectedItem: selectedItem, context: Context
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
    ) {
        items(list) { answer ->
            ItemAnswer(
                answer = answer,
                scope,
                modalBottomSheetState,
                selectedItem,
                context
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ItemAnswer(
    answer: Answer_Model,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    selectedItem: selectedItem, context: Context
) {

    Card(
        modifier = Modifier
            .padding(all = 5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable(
                onClick = {
                    scope.launch {
                        modalBottomSheetState.show()

                        selectedItem.query = answer.query
                        selectedItem.paragraph = answer.paragraph
                        selectedItem.title = answer.title
                        selectedItem.answer = answer.answer
                        selectedItem.aquarcy = answer.accuracy

                    }
                }


            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(15.dp))
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxSize(),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 9.dp),
                ) {

                    Text(text = answer.answer, maxLines = 1, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = answer.paragraph,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray,
                        fontSize = 11.sp, lineHeight = 15.sp
                    )

                }

                Spacer(modifier = Modifier.height(3.dp))

            }


        }
    }
}

class selectedItem() : ViewModel() {
    var query: String by mutableStateOf("")
    var paragraph: String by mutableStateOf("")
    var title: String by mutableStateOf("")
    var answer: String by mutableStateOf("")
    var aquarcy: String by mutableStateOf("")

}