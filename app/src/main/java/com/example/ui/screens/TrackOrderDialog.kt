package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.OrderEntity
import com.example.ui.components.ProductArtwork
import com.example.ui.components.RoyalCrest
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandSurface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TrackingStage(
    val stageNumber: Int,
    val title: String,
    val location: String,
    val description: String,
    val timestamp: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

@Composable
fun TrackOrderDialog(
    initialOrderId: String? = null,
    orders: List<OrderEntity>,
    onDismiss: () -> Unit,
    onContactSupport: () -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var searchInput by remember {
        mutableStateOf(
            initialOrderId ?: orders.firstOrNull()?.orderId ?: "DR-92810"
        )
    }
    var trackedOrderId by remember {
        mutableStateOf(
            initialOrderId ?: orders.firstOrNull()?.orderId ?: "DR-92810"
        )
    }

    LaunchedEffect(initialOrderId) {
        if (!initialOrderId.isNullOrBlank()) {
            searchInput = initialOrderId
            trackedOrderId = initialOrderId
        }
    }

    // Match order or create synthetic active order for custom entered IDs
    val currentOrder = orders.firstOrNull {
        it.orderId.equals(trackedOrderId.trim(), ignoreCase = true)
    } ?: createSyntheticOrder(trackedOrderId.trim())

    val stages = remember(currentOrder) {
        generateTrackingStages(currentOrder)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFDFBF7),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .widthIn(max = 560.dp)
                .testTag("track_order_dialog")
        ) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                // Royal Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(RoyalMaroon, DarkMaroon)
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RoyalCrest(modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Track My Footwear",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = RoyalGold
                                )
                                Text(
                                    text = "Live Marwar Handcraft & Transit Tracking",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_track_order_btn")
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Search Bar
                    Text(
                        text = "Enter Order Identification (ID)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalMaroon
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchInput,
                            onValueChange = { searchInput = it },
                            placeholder = { Text("e.g. DR-92810, DR-48192") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = RoyalMaroon)
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalMaroon,
                                unfocusedBorderColor = Color(0xFFDCC8A0),
                                focusedLabelColor = RoyalMaroon
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focusManager.clearFocus()
                                    if (searchInput.isNotBlank()) {
                                        trackedOrderId = searchInput.trim()
                                    }
                                }
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("order_id_input")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                if (searchInput.isNotBlank()) {
                                    trackedOrderId = searchInput.trim()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("track_submit_button")
                        ) {
                            Text("Track", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Quick Selection Chips of Recent Orders
                    if (orders.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Recent Orders:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            orders.forEach { order ->
                                val isSelected = order.orderId.equals(trackedOrderId, ignoreCase = true)
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) RoyalMaroon else Color.White,
                                    border = BorderStroke(1.dp, if (isSelected) RoyalMaroon else Color(0xFFDCC8A0)),
                                    modifier = Modifier
                                        .clickable {
                                            searchInput = order.orderId
                                            trackedOrderId = order.orderId
                                        }
                                        .testTag("quick_order_chip_${order.orderId}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "#${order.orderId}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = if (isSelected) Color.White else RoyalMaroon
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "• ${formatStatusLabel(order.status)}",
                                            fontSize = 10.5.sp,
                                            color = if (isSelected) RoyalGold else Color(0xFF666666)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CURRENT STATUS HERO CARD
                    StatusHeroCard(
                        order = currentOrder,
                        onCopyAwb = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("AWB Tracking Number", currentOrder.trackingAwb)
                            clipboard.setPrimaryClip(clip)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // PARCEL FOOTWEAR PREVIEW
                    FootwearParcelCard(order = currentOrder)

                    Spacer(modifier = Modifier.height(16.dp))

                    // ARTISAN CRAFTSMANSHIP & SHIPPING TIMELINE
                    Text(
                        text = "Royal Craft & Dispatch Journey",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalMaroon
                        )
                    )
                    Text(
                        text = "From Raw Marwar Leather to Handcrafted Nok & Courier Delivery",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF777777),
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFEBDDC2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("order_status_timeline")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            stages.forEachIndexed { index, stage ->
                                TimelineStepItem(
                                    stage = stage,
                                    isLast = index == stages.size - 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ASSISTANCE & SUPPORT BANNER
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SandSurface,
                        border = BorderStroke(1.dp, Color(0xFFDCC8A0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = RoyalMaroon,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = RoyalGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Need Shipment Assistance?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = RoyalMaroon
                                )
                                Text(
                                    text = "Dedicated Marwar Logistics Desk: +91 98290 12345",
                                    fontSize = 11.sp,
                                    color = Color(0xFF555555)
                                )
                            }
                        }
                    }
                }

                // Bottom Action Footer
                Surface(
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("close_track_modal_footer_btn")
                        ) {
                            Text("Done Tracking")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusHeroCard(
    order: OrderEntity,
    onCopyAwb: () -> Unit
) {
    val (statusColor, badgeBg, statusTitle) = when (order.status.uppercase()) {
        "DELIVERED" -> Triple(Color(0xFF1B5E20), Color(0xFFE8F5E9), "Delivered with Royal Grace ✓")
        "OUT_FOR_DELIVERY" -> Triple(Color(0xFF0D47A1), Color(0xFFE3F2FD), "Out for Doorstep Delivery 🚚")
        "DISPATCHED" -> Triple(RoyalMaroon, Color(0xFFFBE8EC), "In Express Transit ✈️")
        "QUALITY_CHECK" -> Triple(Color(0xFF795548), Color(0xFFEFEBE9), "Quality & Muslin Packaging 🔍")
        "ARTISAN_CRAFTING" -> Triple(DeepGold, Color(0xFFFFF8E7), "Master Karigar Handcrafting 🔨")
        else -> Triple(Color(0xFF333333), Color(0xFFF5F5F5), "Order Confirmed & Leather Sourced 📜")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFDCC8A0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ORDER ID #${order.orderId}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = RoyalMaroon
                    )
                    Text(
                        text = "Placed on ${formatDate(order.timestamp)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF777777),
                            fontSize = 11.sp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBg,
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = statusTitle,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF0E6D2), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Estimated Delivery & Route
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Estimated Delivery",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF888888))
                    )
                    Text(
                        text = order.estimatedDelivery,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (order.status == "DELIVERED") Color(0xFF1B5E20) else RoyalMaroon
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Courier Partner",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF888888))
                    )
                    Text(
                        text = order.courierPartner,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // AWB Number with Copy
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AWB: ",
                        fontSize = 11.sp,
                        color = Color(0xFF777777)
                    )
                    Text(
                        text = order.trackingAwb,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepGold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onCopyAwb() }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = "Copy AWB",
                        tint = RoyalMaroon,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Copy AWB",
                        fontSize = 10.5.sp,
                        color = RoyalMaroon,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Route summary
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SandSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RoyalMaroon,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${order.origin} → ${order.destination}",
                        fontSize = 11.sp,
                        color = Color(0xFF444444),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun FootwearParcelCard(order: OrderEntity) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFDCC8A0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductArtwork(
                productType = order.productType,
                primaryColor = order.productColor,
                modifier = Modifier.size(68.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.itemsSummary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF222222),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Total: ₹${order.totalAmount} • ${order.itemCount} Item(s)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepGold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint = RoyalMaroon,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "100% Bite-Free Leather Guarantee",
                        fontSize = 10.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineStepItem(
    stage: TrackingStage,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Step indicator and connector line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            stage.isCompleted -> Color(0xFF2E7D32)
                            stage.isCurrent -> RoyalMaroon
                            else -> Color(0xFFE0E0E0)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (stage.isCompleted) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else if (stage.isCurrent) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(RoyalGold)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(54.dp)
                        .background(
                            if (stage.isCompleted) Color(0xFF2E7D32).copy(alpha = 0.6f) else Color(0xFFE0E0E0)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Step details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stage.title,
                    fontWeight = if (stage.isCurrent) FontWeight.ExtraBold else FontWeight.Bold,
                    fontSize = 12.5.sp,
                    color = if (stage.isCurrent) RoyalMaroon else if (stage.isCompleted) Color(0xFF222222) else Color(0xFF888888)
                )

                if (stage.timestamp.isNotBlank()) {
                    Text(
                        text = stage.timestamp,
                        fontSize = 10.sp,
                        color = Color(0xFF777777)
                    )
                }
            }

            Text(
                text = stage.location,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.5.sp,
                    color = DeepGold,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stage.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = if (stage.isCurrent || stage.isCompleted) Color(0xFF555555) else Color(0xFF999999)
                )
            )
        }
    }
}

private fun generateTrackingStages(order: OrderEntity): List<TrackingStage> {
    val statusRank = when (order.status.uppercase()) {
        "CONFIRMED" -> 1
        "ARTISAN_CRAFTING" -> 2
        "QUALITY_CHECK" -> 3
        "DISPATCHED" -> 4
        "OUT_FOR_DELIVERY" -> 5
        "DELIVERED" -> 6
        else -> 1
    }

    val baseTime = order.timestamp
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    return listOf(
        TrackingStage(
            stageNumber = 1,
            title = "Order Confirmed & Buff Leather Selected",
            location = "Jaipur Karigar Mandi, RJ",
            description = "Natural vegetable tanned buff leather & silk velvet sourced from heritage tanners.",
            timestamp = sdf.format(Date(baseTime)),
            isCompleted = statusRank > 1,
            isCurrent = statusRank == 1
        ),
        TrackingStage(
            stageNumber = 2,
            title = "Artisan Handcrafting & Zari Stitching",
            location = order.origin,
            description = "Master karigar shaping curved Nok, padding bite-free latex sole cushion, and hand-embroidering golden tilla zari.",
            timestamp = if (statusRank >= 2) sdf.format(Date(baseTime + 18000000L)) else "In Progress",
            isCompleted = statusRank > 2,
            isCurrent = statusRank == 2
        ),
        TrackingStage(
            stageNumber = 3,
            title = "Royal Quality Inspection & Packaging",
            location = "Central Heritage Atelier, Jaipur",
            description = "Double-checked for bite-free flexibility, packed into red velvet muslin dust bag with Certificate of Marwar Craftsmanship.",
            timestamp = if (statusRank >= 3) sdf.format(Date(baseTime + 36000000L)) else "Upcoming",
            isCompleted = statusRank > 3,
            isCurrent = statusRank == 3
        ),
        TrackingStage(
            stageNumber = 4,
            title = "Dispatched via Express Courier",
            location = "Jaipur Airport Cargo Hub, RJ",
            description = "Handed over to ${order.courierPartner}. Airway Bill #${order.trackingAwb} generated and departing transit hub.",
            timestamp = if (statusRank >= 4) sdf.format(Date(baseTime + 54000000L)) else "Upcoming",
            isCompleted = statusRank > 4,
            isCurrent = statusRank == 4
        ),
        TrackingStage(
            stageNumber = 5,
            title = "Out for Doorstep Delivery",
            location = order.destination,
            description = "Courier executive out for final royal delivery with OTP verification.",
            timestamp = if (statusRank >= 5) sdf.format(Date(baseTime + 72000000L)) else "Upcoming",
            isCompleted = statusRank > 5,
            isCurrent = statusRank == 5
        ),
        TrackingStage(
            stageNumber = 6,
            title = "Delivered with Royal Grace",
            location = order.destination,
            description = "Parcel delivered safely to patron. 7-Day Size Exchange guarantee active.",
            timestamp = if (statusRank >= 6) sdf.format(Date(baseTime + 86400000L)) else "Upcoming",
            isCompleted = statusRank >= 6,
            isCurrent = statusRank == 6
        )
    )
}

private fun createSyntheticOrder(orderId: String): OrderEntity {
    val cleanId = if (orderId.startsWith("DR-", ignoreCase = true)) orderId.uppercase() else "DR-$orderId"
    // Deterministic status based on ID hash for demonstration
    val hash = kotlin.math.abs(cleanId.hashCode())
    val possibleStatuses = listOf("CONFIRMED", "ARTISAN_CRAFTING", "QUALITY_CHECK", "DISPATCHED", "OUT_FOR_DELIVERY", "DELIVERED")
    val chosenStatus = possibleStatuses[hash % possibleStatuses.size]

    return OrderEntity(
        orderId = cleanId,
        itemsSummary = "Handcrafted Rajasthani Heritage Mojari",
        itemCount = 1,
        totalAmount = 2499,
        status = chosenStatus,
        statusMessage = "Tracking active for order $cleanId",
        courierPartner = "Blue Dart Royal Air Express",
        trackingAwb = "BLUEDART-RJ-${hash % 900000 + 100000}",
        origin = "Jodhpur Royal Karigar Haveli, RJ",
        destination = "Patron Destination Address, India",
        estimatedDelivery = if (chosenStatus == "DELIVERED") "Delivered Successfully" else "Arriving in 2-3 Days",
        productType = "Mojari",
        productColor = "Maroon",
        timestamp = System.currentTimeMillis() - 259200000L
    )
}

private fun formatStatusLabel(status: String): String {
    return when (status.uppercase()) {
        "DELIVERED" -> "Delivered"
        "OUT_FOR_DELIVERY" -> "Out for Delivery"
        "DISPATCHED" -> "Dispatched"
        "QUALITY_CHECK" -> "Quality Check"
        "ARTISAN_CRAFTING" -> "In Workshop"
        else -> "Confirmed"
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
