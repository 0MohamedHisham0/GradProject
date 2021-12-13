
package com.hti.Grad_Project.Activities.BottomNav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.composebottomnavigation.screen.*
import com.hti.Grad_Project.Activities.BottomNav.BottomNavScreenDataModel.Items.items
import com.hti.Grad_Project.R
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_CATEGORY
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_HOME
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_SAVED_BOOKS
import com.hti.Grad_Project.Utilities.ConstantsBottomNav.ROUTE_SETTING

@Composable
fun NavigationController() {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
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
                                imageVector = if (it.route == ROUTE_SAVED_BOOKS) ImageVector.vectorResource(
                                    id = R.drawable.icon_book
                                ) else it.icon,

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

            }
        },
        drawerContent = {
            Drawer()
        }
    ) {
        ScreenController(navController = navController)
    }
}

@Composable
fun ScreenController(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ROUTE_HOME) {
        composable(ROUTE_HOME) {
            Home()
        }
        composable(ROUTE_CATEGORY) {
            Category()
        }
        composable(ROUTE_SAVED_BOOKS) {
            SavedBooks()
        }
        composable(ROUTE_SETTING) {
            Settings()
        }
    }
}

@Composable
fun TopBar(onMenuClicked: () -> Unit, title: String) {
    TopAppBar( backgroundColor = MaterialTheme.colors.background, modifier = Modifier.height(90.dp),

        title = {
            Text(text = title)
        },
        navigationIcon = {
            Spacer(modifier = Modifier.width(15.dp))
            Surface(
                modifier = Modifier
                    .clickable(onClick = onMenuClicked)
                    .clip(RoundedCornerShape(5.dp)).size(27.dp),
                color = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu Btn", tint = Color.Black
                )
            }

        }
    )
}

@Composable
fun Drawer() {
    // Column Composable
    Column(
        Modifier
            .background(androidx.compose.ui.graphics.Color.White)
            .fillMaxSize()
    ) {
        // Repeat is a loop which
        // takes count as argument
        repeat(5) { item ->
            Text(text = "Item number $item", modifier = Modifier.padding(8.dp), color = Color.Black)
        }
    }
}
