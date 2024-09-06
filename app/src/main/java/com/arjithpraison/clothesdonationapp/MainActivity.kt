// MainActivity.kt
package com.arjithpraison.clothesdonationapp

import DonateScreen
import RequestScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arjithpraison.clothesdonationapp.ui.theme.ClothesDonationAppTheme
import com.arjithpraison.clothesdonationapp.ui.theme.ExchangeScreen
import com.arjithpraison.clothesdonationapp.ui.theme.LendScreen
import com.arjithpraison.clothesdonationapp.ui.theme.MainScreen
import com.arjithpraison.clothesdonationapp.ui.theme.ReceiveScreen
import com.arjithpraison.clothesdonationapp.ui.theme.SplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClothesDonationAppTheme {
                // Create NavController to manage navigation
                val navController = rememberNavController()

                // State to hold fetched data
                var jsonData by remember { mutableStateOf("Fetching data...") }

                // Fetch data when the Composable is first launched
                LaunchedEffect(Unit) {
                    fetchData { data ->
                        jsonData = data
                    }
                }

                // Setup the navigation graph with fetched data
                NavGraph(navController = navController, jsonData = jsonData)
            }
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, jsonData: String) {
    // Setup NavHost with start destination and composable screens
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("main") { MainScreen(navController = navController) }
        composable("donate") { DonateScreen(navController = navController) }
        composable("request") { RequestScreen(navController = navController) }
        composable("lend") { LendScreen(navController = navController) }
        composable("exchange") { ExchangeScreen(navController = navController)}
        // Add other screens here
    }

}

private fun fetchData(onResult: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://164.8.36.217:5000/receive_data_donate") // Replace with your server URL
            .build()

        client.newCall(request).execute().use { response ->
            val data = response.body?.string() ?: "No data available"
            withContext(Dispatchers.Main) {
                onResult(data)
            }
        }
    }
}
