package com.hti.Grad_Project.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hti.Grad_Project.Activities.BottomNav.NavigationController
import com.hti.Grad_Project.Activities.ui.theme.ComposeBottomNavigationTheme
import com.hti.Grad_Project.Utilities.Constants

@ExperimentalMaterialApi
class BottomNavContainerScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                val context = LocalContext.current
                if (Constants.checkInternetConnection(context))
                    ComposeBottomNavigationTheme {
                        Surface(color = MaterialTheme.colors.background) {
                            NavigationController()
                        }
                    }
                else
                    snackBarDemo()
            }
        }

}

@Composable
fun snackBarDemo() {

    Snackbar(
        action = {

        },
        modifier = Modifier.padding(8.dp)
    ) { Text(text = "No Internet connection!") }

}
