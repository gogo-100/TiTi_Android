package com.titi.app.feature.time.navigation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.titi.app.core.util.fromJson
import com.titi.app.feature.time.SplashResultState
import com.titi.app.feature.time.ui.stopwatch.StopWatchScreen
import com.titi.app.feature.time.ui.timer.TimerScreen

const val TIME_GRAPH_START_ARG = "startDestination"
const val TIME_GRAPH_SPLASH_ARG = "splashSearchResult"
const val TIME_GRAPH_FINISH_ARG = "isFinish"

const val TIME_GRAPH_SCREEN = "time"
const val TIME_GRAPH_ROUTE =
    "$TIME_GRAPH_SCREEN/{$TIME_GRAPH_START_ARG}?$TIME_GRAPH_SPLASH_ARG={$TIME_GRAPH_SPLASH_ARG}&$TIME_GRAPH_FINISH_ARG={$TIME_GRAPH_FINISH_ARG}"

const val TIMER_SCREEN = "timer"
const val TIMER_ROUTE = "$TIME_GRAPH_SCREEN/$TIMER_SCREEN"
const val STOPWATCH_SCREEN = "stopWatch"
const val STOPWATCH_ROUTE = "$TIME_GRAPH_SCREEN/$STOPWATCH_SCREEN"

fun NavController.navigateToTimeGraph(
    route: String,
    navOptions: NavOptions
) = navigate(route, navOptions)

fun NavController.navigateToTimer(navOptions: NavOptions) =
    navigate(TIMER_ROUTE, navOptions)

fun NavController.navigateToStopWatch(navOptions: NavOptions) =
    navigate(STOPWATCH_ROUTE, navOptions)

fun makeTimeRoute(
    startDestination: String,
    splashScreenResult: String? = null,
    isFinish: Boolean = false
) =
    "$TIME_GRAPH_SCREEN/$startDestination?$TIME_GRAPH_SPLASH_ARG=$splashScreenResult&$TIME_GRAPH_FINISH_ARG=$isFinish"

fun NavGraphBuilder.timeGraph(
    navController: NavHostController,
    startDestination: String,
    splashResultState: SplashResultState,
    //  nestedGraphs: NavGraphBuilder.() -> Unit,
    onNavigateToColor: (Int) -> Unit,
    onNavigateToMeasure: (String) -> Unit,
) {
    navigation(
        route = TIME_GRAPH_ROUTE,
        startDestination = "$TIME_GRAPH_SCREEN/$startDestination",
        arguments = listOf(
            navArgument(TIME_GRAPH_SPLASH_ARG) {
                NavType.StringType
                nullable = true
            },
            navArgument(TIME_GRAPH_FINISH_ARG) {
                NavType.BoolType
                defaultValue = false
            }
        )
    ) {
        composable(route = TIMER_ROUTE) { backstackEntry ->
            val parentBackStackEntry = remember(backstackEntry) {
                navController.getBackStackEntry(TIME_GRAPH_ROUTE)
            }

            val splashResultStateFromBackStackEntry = parentBackStackEntry
                .arguments
                ?.getString(TIME_GRAPH_SPLASH_ARG)
                ?.fromJson<SplashResultState>()
                ?: splashResultState

            var isFinishState by remember {
                mutableStateOf(
                    parentBackStackEntry
                        .arguments
                        ?.getBoolean(TIME_GRAPH_FINISH_ARG)
                        ?: false
                )
            }

            Log.e("ABC", splashResultStateFromBackStackEntry.toString())

            TimerScreen(
                splashResultState = splashResultStateFromBackStackEntry,
                isFinish = isFinishState,
                onChangeFinishStateFalse = { isFinishState = false },
                onNavigateToColor = { onNavigateToColor(1) },
                onNavigateToMeasure = onNavigateToMeasure
            )
        }

        composable(route = STOPWATCH_ROUTE) { backstackEntry ->
            val parentBackStackEntry = remember(backstackEntry) {
                navController.getBackStackEntry(TIME_GRAPH_ROUTE)
            }

            val splashResultStateFromBackStackEntry = parentBackStackEntry
                .arguments
                ?.getString(TIME_GRAPH_SPLASH_ARG)
                ?.fromJson<SplashResultState>()
                ?: splashResultState
            Log.e("ABCD", splashResultStateFromBackStackEntry.toString())
            StopWatchScreen(
                splashResultState = splashResultStateFromBackStackEntry,
                onNavigateToColor = { onNavigateToColor(2) },
                onNavigateToMeasure = onNavigateToMeasure
            )
        }

        //    nestedGraphs()
    }
}