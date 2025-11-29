package ka.mobile.hidrify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ka.mobile.hidrify.ui.history.HistoryDetailScreen
import ka.mobile.hidrify.ui.reminder.ReminderSettingScreen
import ka.mobile.hidrify.ui.theme.HidrifyTheme
import ka.mobile.hidrify.ui.tracker.TrackerScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HidrifyTheme {
                val navController = rememberNavController()

                // Definisi Route Navigasi
                NavHost(navController = navController, startDestination = "tracker_screen") {

                    // Route A: Halaman Tracker
                    composable("tracker_screen") {
                        TrackerScreen(navController = navController)
                    }

                    // Route B: Nanti kita buat di langkah selanjutnya
                    composable("reminder_screen") {
                        ReminderSettingScreen(navController = navController)
                    }

                    // Route C: Halaman Detail History
                    composable(
                        route = "history_detail/{logId}",
                        arguments = listOf(navArgument("logId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val logId = backStackEntry.arguments?.getInt("logId") ?: 0
                        HistoryDetailScreen(
                            navController = navController,
                            logId = logId
                        )
                    }
                }
            }
        }
    }
}