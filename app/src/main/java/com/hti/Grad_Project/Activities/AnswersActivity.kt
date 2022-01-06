package com.hti.Grad_Project.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hti.Grad_Project.Activities.ui.theme.ComposeBottomNavigationTheme
import com.hti.Grad_Project.LocalData.answerList
import com.hti.Grad_Project.Model.Answer_Model
import com.hti.Grad_Project.Model.QuestionAsk_Model
import com.hti.Grad_Project.Model.answerModel
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AnswersActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()

    @ExperimentalMaterialApi
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
                        getAnswerLiveData(mainViewModel.answerListModel, questionModel)
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
    questionModel: QuestionAsk_Model
) {

    val personList by personListLiveData.observeAsState(initial = emptyList())
    if (personList.isEmpty()) {
        Loading()
    } else {
        AnswerScreen(personList, questionModel)
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
@Preview("answer Preview")
@Composable
fun AnswersPreview() {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {

            val questionModel = QuestionAsk_Model("What is your name?", "Bert", 10, "fieojfoejfe")
            AnswerScreen(list = answerList.answerBertList, questionModel = questionModel)

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun AnswerScreen(
    list: List<Answer_Model>,
    questionModel: QuestionAsk_Model
) {

    val question: String = questionModel.question
    val context = LocalContext.current

    val IsBertClicked = remember { mutableStateOf(true) }

    val ColorOfBert = remember { mutableStateOf(R.color.orange_main) }
    val ColorOfDistlBert = remember { mutableStateOf(R.color.orange_main) }

    val myList = remember { mutableStateListOf<answerModel>() }
    val firstList = remember {
        answerList.answerBertList
    }
    val currentListSize = remember { mutableStateOf(questionModel.prediction) }


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

        TopBarAnswerScreen(onBackButtonClick = { /*TODO*/ }, ParagraphName = questionModel.question)

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
            selectedItem = selectedItem
        )
        currentListSize.value = list.size
    }

    ModelBottomSheet(
        scope = scope, modalBottomSheetState = modalBottomSheetState, selectedItem
    )

}


@Composable
@ExperimentalMaterialApi
fun ModelBottomSheet(
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    model: selectedItem
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Diver
                    Divider(
                        color = Color.LightGray,
                        thickness = 3.dp,
                        modifier = Modifier.width(50.dp)
                    )

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
    selectedItem: selectedItem
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
    ) {
        items(list) { answer ->
            ItemAnswer(answer = answer, scope, modalBottomSheetState, selectedItem)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ItemAnswer(
    answer: Answer_Model,
    scope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    selectedItem: selectedItem
) {
    Card(
        modifier = Modifier
            .padding(all = 5.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    scope.launch {
                        modalBottomSheetState.show()

                        selectedItem.answer = answer.answer
                        selectedItem.paragraph = answer.paragraph
                        selectedItem.aquarcy = answer.accuracy

                    }
                }


            ),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(15.dp))
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 14.dp),
                    verticalArrangement = Arrangement.Center
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

            }


        }
    }
}

class selectedItem() : ViewModel() {
    var paragraph: String by mutableStateOf("")
    var answer: String by mutableStateOf("")
    var aquarcy: String by mutableStateOf("")
}