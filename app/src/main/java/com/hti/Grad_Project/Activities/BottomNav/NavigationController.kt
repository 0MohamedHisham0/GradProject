package com.hti.Grad_Project.Activities.BottomNav

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hti.Grad_Project.Activities.BottomNav.BottomNavScreenDataModel.Items.items
import com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens.*
import com.hti.Grad_Project.Activities.ui.theme.ClearRippleTheme
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_HOME
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_GOOGLE_ENHANCED
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_SAVED_PDF
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_SAVED_QUESTIONS

@ExperimentalMaterialApi
@Composable
fun NavigationController() {
    //for dialog
    val context = LocalContext.current
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }


    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {

                CompositionLocalProvider(
                    LocalRippleTheme provides ClearRippleTheme
                ) {
                    BottomAppBar(
                        modifier = Modifier.clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                        cutoutShape = CircleShape,
                        backgroundColor = MaterialTheme.colors.background,
                        elevation = 16.dp
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()           //when ever backStack changes it will recompose itself
                        val currentRoute =
                            navBackStackEntry?.destination?.route                        //fetching current backStack entry
                        Spacer(modifier = Modifier.width(0.dp))

                        items.forEach {
                            if (it == items[2]) {
                                Spacer(modifier = Modifier.width(60.dp))
                            }

                            BottomNavigationItem(
                                icon = {                                                                //bottom nav icon
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = it.icon),
                                        contentDescription = "",
                                        tint = if (currentRoute == it.route) colorResource(id = R.color.orange_main) else Color.DarkGray
                                    )
                                },
                                selected = currentRoute == it.route,
                                //current destination that is visible to user
                                label = {
                                    if (currentRoute == it.route) {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(20.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CircleShape()

                                        }
                                    } else {

                                        Text(                                                               //bottom nav text
                                            text = it.label,
                                            color = if (currentRoute == it.route) Color.LightGray else Color.DarkGray,
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                            fontSize = 10.sp
                                        )
                                    }
                                },
                                onClick = {
                                    if (currentRoute != it.route) {                                     //current route is not equal to same route
                                        navController.graph.startDestinationRoute?.let { item ->        //then handle back press
                                            navController.popBackStack(
                                                item, false
                                            )
                                        }
                                    }
                                    if (currentRoute != it.route) {                                     //condition to check current route is not equal to screens route
                                        it.route?.let { it1 -> navController.navigate(it1) }
                                    }
                                },
                                alwaysShowLabel = true,                                                 // showing/hiding title text
                                selectedContentColor = MaterialTheme.colors.secondary,                  // ripple color
                            )
                        }

                        BottomNavigationItem(
                            icon = {                                                                //bottom nav icon
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_google),
                                    contentDescription = "",
                                    tint = if (currentRoute == ROUTE_GOOGLE_ENHANCED) colorResource(
                                        id = R.color.orange_main
                                    ) else Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                            },
                            selected = currentRoute == ROUTE_GOOGLE_ENHANCED,                                    //current destination that is visible to user
                            label = {
                                if (currentRoute == ROUTE_GOOGLE_ENHANCED) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(20.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircleShape()

                                    }
                                } else {
                                    Text(                                                               //bottom nav text
                                        text = "G Enhanced",
                                        color = if (currentRoute == ROUTE_GOOGLE_ENHANCED) colorResource(
                                            id = R.color.orange_main
                                        ) else Color.DarkGray,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center,
                                        fontSize = 10.sp
                                    )
                                }

                            },
                            onClick = {
                                if (currentRoute != ROUTE_GOOGLE_ENHANCED) {                                     //current route is not equal to same route
                                    navController.graph.startDestinationRoute?.let { item ->        //then handle back press
                                        navController.popBackStack(
                                            item, false
                                        )
                                    }
                                }
                                if (currentRoute != ROUTE_GOOGLE_ENHANCED) {                                     //condition to check current route is not equal to screens route
                                    ROUTE_GOOGLE_ENHANCED.let { it1 -> navController.navigate(it1) }
                                }
                            },
                            alwaysShowLabel = true,                                                 // showing/hiding title text
                            selectedContentColor = MaterialTheme.colors.secondary,                  // ripple color
                        )

                    }

                }

            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = { setShowDialog(true) },
                    contentColor = Color.White,
                    backgroundColor = colorResource(id = R.color.orange_main)
                ) {
                    Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add icon")
                }

            }
        ) { innerPadding ->
            DialogAddingNewPDF(showDialog, setShowDialog, context)
            Box(modifier = Modifier.padding(innerPadding)) {

                ScreenController(navController = navController)
            }
        }

    }
}

@Preview
@Composable
fun CircleShape() {
    val color = colorResource(id = R.color.orange_main)
    androidx.compose.foundation.Canvas(modifier = Modifier.size(19.dp), onDraw = {

        drawPath(
            color = color,
            path = trianglePath,
        )
    })
}

val trianglePath = Path().apply {
    val size = 50F
    // Moves to top center position
    moveTo(size / 2f, 20f)
    // Add line to bottom right corner
    lineTo(size, size)
    // Add line to bottom left corner
    lineTo(0f, size)
}

@ExperimentalMaterialApi
@Composable
fun ScreenController(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ROUTE_HOME) {
        composable(ROUTE_HOME) {
            Home()
        }
        composable(ROUTE_SAVED_PDF) {
            MyPdf()
        }
        composable(ROUTE_SAVED_QUESTIONS) {
            MySavedQuestions()
        }
        composable(ROUTE_GOOGLE_ENHANCED) {
            GoogleEnhanced()
        }
    }
}

class CustomShape : Shape {
    override fun createOutline(size: Size, density: Density): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

interface Shape {
    fun createOutline(size: Size, density: Density): Outline
}
