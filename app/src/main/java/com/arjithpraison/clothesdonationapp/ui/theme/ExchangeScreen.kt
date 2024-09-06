package com.arjithpraison.clothesdonationapp.ui.theme


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun ExchangeScreen(navController: NavController) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    // State for Size Dropdown
    var expandedSize by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf("Select Size") }
    val sizes = listOf("XXS", "XS", "S", "M", "L", "XL", "XXL")

    // State for Condition Dropdown
    var expandedCondition by remember { mutableStateOf(false) }
    var selectedCondition by remember { mutableStateOf("Select Condition") }
    val conditions = listOf("New", "Used", "Torn")

    // State for Type Dropdown
    var expandedType by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("Select Type") }
    val types = listOf("Shirt", "T-Shirt", "Jacket", "Trousers", "Dress", "Shorts")

    // State for Category Dropdown
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Select Category") }
    val categories = listOf("Women", "Men", "Kids", "Baby")

    // State for Location
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var locationText by remember { mutableStateOf("Select Location") }
    var isLocationButtonEnabled by remember { mutableStateOf(true) }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                getCurrentLocation(context) { lat, lon ->
                    latitude = lat
                    longitude = lon
                    locationText = "Location: $lat, $lon"
                    isLocationButtonEnabled = false
                }
            } else {
                // Handle permission denied scenario
                locationText = "Permission Denied"
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Exchange Details Section
        Text(text = "Exchange Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Title input
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Description input
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Size Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedSize,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedSize = true },
                enabled = false,
                label = { Text("Select Size") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = expandedSize,
                onDismissRequest = { expandedSize = false }
            ) {
                sizes.forEach { size ->
                    DropdownMenuItem(
                        text = { Text(text = size) },
                        onClick = {
                            selectedSize = size
                            expandedSize = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Condition Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCondition,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedCondition = true },
                enabled = false,
                label = { Text("Select Condition") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = expandedCondition,
                onDismissRequest = { expandedCondition = false }
            ) {
                conditions.forEach { condition ->
                    DropdownMenuItem(
                        text = { Text(text = condition) },
                        onClick = {
                            selectedCondition = condition
                            expandedCondition = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Type Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedType = true },
                enabled = false,
                label = { Text("Select Type") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(text = type) },
                        onClick = {
                            selectedType = type
                            expandedType = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Category Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedCategory = true },
                enabled = false,
                label = { Text("Select Category") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category) },
                        onClick = {
                            selectedCategory = category
                            expandedCategory = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Location Button
        Button(
            onClick = {
                // Request location permission
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    // If permission is already granted, get the current location
                    getCurrentLocation(context) { lat, lon ->
                        latitude = lat
                        longitude = lon
                        locationText = "Location: $lat, $lon"
                        isLocationButtonEnabled = false
                    }
                }
            },
            enabled = isLocationButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(locationText)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                submitData(
                    context,
                    title.text,
                    description.text,
                    selectedSize,
                    selectedCondition,
                    selectedType,
                    selectedCategory,
                    latitude,
                    longitude
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Exchange")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Request Details Section
        Text(text = "Request Details", style = MaterialTheme.typography.titleLarge)
        // Here you would display the request details or a list of requests.
        // You might use a LazyColumn to display requests if you have them.

    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: android.content.Context, onLocationReceived: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocationReceived(it.latitude, it.longitude)
        }
    }
}

fun submitData(
    context: Context,
    title: String,
    description: String,
    size: String,
    condition: String,
    type: String,
    category: String,
    latitude: Double?,
    longitude: Double?
) {
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL("http://164.8.36.217:5000/receive_data_exchange") // Updated endpoint for exchanges
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        val jsonInputString = JSONObject().apply {
            put("title", title)
            put("description", description)
            put("size", size)
            put("condition", condition)
            put("type", type)
            put("category", category)
            latitude?.let { put("latitude", it) }
            longitude?.let { put("longitude", it) }
        }.toString()

        try {
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonInputString)
                writer.flush()
            }

            val responseCode = connection.responseCode
            withContext(Dispatchers.Main) {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Toast.makeText(context, "Exchange submitted successfully.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error: $responseCode", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Submission failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } finally {
            connection.disconnect()
        }
    }
}
