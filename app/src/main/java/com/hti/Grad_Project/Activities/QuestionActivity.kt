package com.hti.Grad_Project.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SwitchDefaults.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hti.Grad_Project.Activities.ui.theme.ComposeBottomNavigationTheme
import com.hti.Grad_Project.Activities.ui.theme.GradProjectTheme
import com.hti.Grad_Project.R
import kotlinx.coroutines.launch

class QuestionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ComposeBottomNavigationTheme {
                Surface(color = colors.background) {
                    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
                        ScaffoldQuestionScreen(
                            onBackButtonClick = { onBackPressed() },
                            ParagraphName = "Food Book",
                            onMicClicked = { TODO() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScaffoldQuestionScreen(
    onBackButtonClick: () -> Unit,
    onMicClicked: () -> Unit,
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
                onMicClicked = onMicClicked
            )
        },

        )
}

@Composable
fun QuestionScreen(onBackButtonClick: () -> Unit, onMicClicked: () -> Unit, ParagraphName: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        //Var
        var textFieldState_Question by remember {
            mutableStateOf("")
        }
        var textFieldState_Predictions by remember {
            mutableStateOf("")
        }
        val isCheckedDistlBert = remember { mutableStateOf(false) }
        val isCheckedBert = remember { mutableStateOf(false) }

        //Views

        Spacer(modifier = Modifier.height(16.dp))

        TopBarQuestionScreen(
            onBackButtonClick = { onBackButtonClick },
            ParagraphName = ParagraphName
        )

        Spacer(modifier = Modifier.size(30.dp))

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
                        .size(25.dp), onClick = onMicClicked
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_mic),
                        contentDescription = "Back Button",
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

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                modifier = Modifier
                    .height(50.dp)
                    .width(100.dp)
                    .fillMaxWidth(),
                value = textFieldState_Predictions,
                placeholder = { Text(text = "1") },
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
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp), shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.orange_main))
            ) {
                Text(text = "Submit", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.size(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                onCheckedChange = { checked -> isCheckedDistlBert.value = checked },
                colors = colors(
                    checkedThumbColor = colorResource(id = R.color.orange_main)
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column() {
                Text(text = "Bert", fontSize = 20.sp)
                Text(text = "Deeper", fontSize = 13.sp, color = Color.DarkGray)
            }

            Switch(
                checked = isCheckedBert.value,
                onCheckedChange = { checked -> isCheckedBert.value = checked },
                colors = colors(
                    checkedThumbColor = colorResource(id = R.color.orange_main)
                )
            )
        }


    }
}

@Composable
@Preview
fun PreviewScreen() {
    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
        QuestionScreen(
            onBackButtonClick = { /*TODO*/ },
            onMicClicked = { /*TODO*/ },
            ParagraphName = "Food Book"
        )
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

