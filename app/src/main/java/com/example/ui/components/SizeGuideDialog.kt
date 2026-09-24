package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandSurface

data class SizeRow(
    val ukIn: String,
    val eu: String,
    val usMen: String,
    val usWomen: String,
    val footLengthCm: String
)

@Composable
fun SizeGuideDialog(onDismiss: () -> Unit) {
    val sizeTable = listOf(
        SizeRow("UK / IN 6", "EU 39-40", "US 7.0", "US 8.0", "24.5 cm"),
        SizeRow("UK / IN 7", "EU 40-41", "US 8.0", "US 9.0", "25.4 cm"),
        SizeRow("UK / IN 8", "EU 42", "US 9.0", "US 10.0", "26.2 cm"),
        SizeRow("UK / IN 9", "EU 43", "US 10.0", "US 11.0", "27.1 cm"),
        SizeRow("UK / IN 10", "EU 44", "US 11.0", "US 12.0", "27.9 cm"),
        SizeRow("UK / IN 11", "EU 45", "US 12.0", "US 13.0", "28.8 cm")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold),
            modifier = Modifier.fillMaxWidth().testTag("size_guide_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Straighten,
                            contentDescription = null,
                            tint = RoyalMaroon,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Footwear Size Guide",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoyalMaroon
                            )
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "Dharti Rajasthan footwear follows standard UK / Indian sizing.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666))
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkMaroon)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("UK/IN", color = RoyalGold, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1f))
                    Text("EU", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1f))
                    Text("US (M/W)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.2f))
                    Text("Foot Length", color = RoyalGold, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.2f))
                }

                // Table Rows
                sizeTable.forEachIndexed { index, row ->
                    val bg = if (index % 2 == 0) SandSurface else Color.White
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bg)
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(row.ukIn, fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = RoyalMaroon, modifier = Modifier.weight(1f))
                        Text(row.eu, fontSize = 11.sp, color = Color(0xFF333333), modifier = Modifier.weight(1f))
                        Text("${row.usMen} / ${row.usWomen}", fontSize = 11.sp, color = Color(0xFF333333), modifier = Modifier.weight(1.2f))
                        Text(row.footLengthCm, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color(0xFF2E7D32), modifier = Modifier.weight(1.2f))
                    }
                    HorizontalDivider(color = Color(0xFFEADBBE), thickness = 0.5.dp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Artisan Break-in advice
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF9E6),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = RoyalMaroon,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Artisan Fit & Break-in Tip",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalMaroon
                                )
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Traditional Mojaris & Juttis do not have distinct left/right feet initially. Within 2-3 hours of wearing, our soft vegetable-tanned leather naturally conforms to each foot's shape.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    color = Color(0xFF444444)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Understood", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
