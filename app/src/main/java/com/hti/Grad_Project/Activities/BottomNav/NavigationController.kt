package com.hti.Grad_Project.Activities.BottomNav

import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hti.Grad_Project.Activities.BottomNav.BottomNavScreenDataModel.Items.items
import com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens.Home
import com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens.MyPdf
import com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens.MySavedQuestions
import com.hti.Grad_Project.Activities.BottomNav.BottomNav_Screens.RippleCustomTheme
import com.hti.Grad_Project.Activities.ui.theme.ClearRippleTheme
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_CATEGORY
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_HOME
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_SAVED_BOOKS

@ExperimentalMaterialApi
@Composable
fun NavigationController() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                CompositionLocalProvider(
                    LocalRippleTheme provides ClearRippleTheme
                ) {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 16.dp
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()           //when ever backStack changes it will recompose itself
                    val currentRoute =
                        navBackStackEntry?.destination?.route                        //fetching current backStack entry

                    items.forEach {
                        BottomNavigationItem(
                            icon = {                                                                //bottom nav icon
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = it.icon),
                                    contentDescription = "",
                                    tint = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                                )
                            },
                            selected = currentRoute == it.route,                                    //current destination that is visible to user
                            label = {
                                Text(                                                               //bottom nav text
                                    text = it.label,
                                    color = if (currentRoute == it.route) Color.DarkGray else Color.LightGray,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
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
                                    navController.navigate(it.route)
                                }
                            },
                            alwaysShowLabel = true,                                                 // showing/hiding title text
                            selectedContentColor = MaterialTheme.colors.secondary,                  // ripple color
                        )
                    }

                }}
            },
        ) {
            ScreenController(navController = navController)
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun ScreenController(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ROUTE_HOME) {
        composable(ROUTE_HOME) {
            Home()
        }
        composable(ROUTE_CATEGORY) {
            MyPdf()
        }
        composable(ROUTE_SAVED_BOOKS) {
            MySavedQuestions()
        }
    }
}


