// PlaceholderItem.kt
package com.arjithpraison.clothesdonationapp.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlaceholderItem(item: ItemData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Title with larger font
            Text(
                text = item.title,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Details
            Text(text = "Size: ${item.size}", fontSize = 16.sp)
            Text(text = "Description: ${item.description}", fontSize = 16.sp)
            Text(text = "Category: ${item.category}", fontSize = 16.sp)
            Text(text = "Condition: ${item.condition}", fontSize = 16.sp)
        }
    }
}
