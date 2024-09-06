package com.arjithpraison.clothesdonationapp.ui.theme

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    var sampleData by remember { mutableStateOf(listOf<ItemData>()) }
    var selectedItem by remember { mutableStateOf<ItemData?>(null) }
    var isReceiveButtonEnabled by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        fetchData { fetchedItems ->
            sampleData = fetchedItems
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 160.dp),  // Adjusted padding to fit more buttons
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sampleData) { item ->
                val isSelected = selectedItem == item
                val backgroundColor = if (isSelected) Color.LightGray else Color.White

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(backgroundColor)
                        .clickable {
                            selectedItem = if (selectedItem == item) null else item
                            isReceiveButtonEnabled = selectedItem != null
                        }
                        .padding(16.dp)
                ) {
                    PlaceholderItem(item = item)
                }
            }
        }

        // Add the buttons at the bottom of the screen
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between rows
        ) {
            // Request and Lend buttons in one row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.navigate("request")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Request")
                }

                Button(
                    onClick = {
                        navController.navigate("lend")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(text = "Lend")
                }
            }

            // Donate and Receive buttons in one row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.navigate("donate")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Donate")
                }

                Button(
                    onClick = {
                        selectedItem?.let {
                            openGoogleMaps(context, it.latitude, it.longitude)
                        }
                    },
                    enabled = isReceiveButtonEnabled,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(text = "Receive")
                }
            }
        }
    }
}

fun fetchData(onResult: (List<ItemData>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://164.8.36.217:5000/receive_data_donate") // Replace with your server URL
            .build()

        client.newCall(request).execute().use { response ->
            val jsonString = response.body?.string() ?: "{}"
            val itemList = parseJsonData(jsonString)
            withContext(Dispatchers.Main) {
                onResult(itemList)
            }
        }
    }
}

fun parseJsonData(jsonString: String): List<ItemData> {
    val items = mutableListOf<ItemData>()
    try {
        val jsonObject = JSONObject(jsonString)

        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val itemObject = jsonObject.getJSONObject(key)

            val title = itemObject.optString("title", "Unknown")
            val size = itemObject.optString("size", "Unknown")
            val description = itemObject.optString("description", "Unknown")
            val category = itemObject.optString("category", "Unknown")
            val condition = itemObject.optString("condition", "Unknown")
            val latitude = itemObject.optDouble("latitude", 0.0)
            val longitude = itemObject.optDouble("longitude", 0.0)

            items.add(ItemData(title, size, description, category, condition, latitude, longitude))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return items
}

fun openGoogleMaps(context: Context, latitude: Double, longitude: Double) {
    val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent) // Use context to start the activity
}
