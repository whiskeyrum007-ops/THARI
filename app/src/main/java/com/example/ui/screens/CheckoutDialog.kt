package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.RoyalCrest
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandCream
import com.example.ui.theme.SandSurface

data class PaymentOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

@Composable
fun CheckoutDialog(
    totalAmount: Int,
    itemCount: Int,
    pointsRedeemed: Int = 0,
    onOrderPlaced: (orderId: String, totalPaid: Int, pointsRedeemed: Int) -> Unit,
    onTrackOrder: (orderId: String) -> Unit = {},
    onDismiss: () -> Unit
) {
    var fullName by remember { mutableStateOf("Padmashree Singh") }
    var phone by remember { mutableStateOf("+91 98290 12345") }
    var address by remember { mutableStateOf("Haveli No. 4, Near Hawa Mahal, Johari Bazaar") }
    var city by remember { mutableStateOf("Jaipur") }
    var state by remember { mutableStateOf("Rajasthan") }
    var pincode by remember { mutableStateOf("302001") }

    var selectedPayment by remember { mutableStateOf("UPI") }
    var isProcessing by remember { mutableStateOf(false) }
    var orderPlacedSuccess by remember { mutableStateOf(false) }
    val orderId = remember { "DR-" + (10000..99999).random() }
    val earnedPoints = remember(totalAmount) { ((totalAmount * 10) / 100).coerceAtLeast(25) }

    val paymentOptions = listOf(
        PaymentOption("UPI", "Instant UPI / QR", "GPay, PhonePe, Paytm, BHIM", Icons.Default.QrCode),
        PaymentOption("CARD", "Credit / Debit Card", "Visa, Mastercard, RuPay", Icons.Default.CreditCard),
        PaymentOption("NETBANK", "Netbanking", "All Indian National & Private Banks", Icons.Default.AccountBalance),
        PaymentOption("COD", "Cash on Delivery", "Pay in cash or UPI at delivery", Icons.Default.Payments)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, RoyalGold, RoundedCornerShape(20.dp))
                .testTag("checkout_dialog"),
            color = Color.White
        ) {
            if (orderPlacedSuccess) {
                // Order Success Receipt View
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE8F5E9),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF2E7D32)),
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Khamma Ghani! Order Confirmed",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalMaroon
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Order $orderId has been placed with our master karigars",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF666666), textAlign = TextAlign.Center)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Royal Points Earned Spotlight
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF8E7),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
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
                                        Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = RoyalGold,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "+$earnedPoints Royal Points Earned! 👑",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = RoyalMaroon
                                )
                                Text(
                                    text = "Credited to your Royal Rewards balance (1 pt = ₹1 discount)",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666), fontSize = 11.sp)
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SandSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Amount Paid / Due:", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666)))
                                Text("₹$totalAmount", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = RoyalMaroon))
                            }

                            if (pointsRedeemed > 0) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Points Redeemed:", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF2E7D32)))
                                    Text("-₹$pointsRedeemed", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)))
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Payment Method:", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666)))
                                Text(selectedPayment, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Address:", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666)))
                                Text("$city, $pincode", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Estimated Arrival:", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666)))
                                Text("In 3 to 4 days", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32)))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Includes: Dharti Rajasthan Certificate of Authenticity, Muslin dust bag & 7-Day Size Exchange Guarantee.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DeepGold,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            onTrackOrder(orderId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("track_new_order_btn")
                    ) {
                        Text("🚚 Track My Footwear Order", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    androidx.compose.material3.OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalMaroon),
                        modifier = Modifier.fillMaxWidth().testTag("continue_shopping_btn")
                    ) {
                        Text("Continue Shopping", color = RoyalMaroon, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Checkout Form
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkMaroon)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RoyalCrest(modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Secure Checkout",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = RoyalGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        // Trust & Security badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "256-Bit SSL Encrypted • 100% Authentic Rajasthani Leather Guarantee",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF1B5E20),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        if (pointsRedeemed > 0) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFF7E0),
                                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Stars, contentDescription = null, tint = RoyalMaroon, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "₹$pointsRedeemed Royal Rewards Discount Applied at Checkout",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        color = RoyalMaroon
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Shipping Details
                        Text(
                            text = "Shipping Address",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = RoyalMaroon)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("checkout_name_input"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Mobile Number") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("checkout_phone_input"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Street Address / Landmark") },
                            modifier = Modifier.fillMaxWidth().testTag("checkout_address_input"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("City") },
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("checkout_city_input"),
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = pincode,
                                onValueChange = { pincode = it },
                                label = { Text("Pincode") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("checkout_pincode_input"),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Payment Methods
                        Text(
                            text = "Select Payment Method",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = RoyalMaroon)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        paymentOptions.forEach { opt ->
                            val isSelected = selectedPayment == opt.id
                            Surface(
                                onClick = { selectedPayment = opt.id },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFFFFF7E6) else SandSurface,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) RoyalMaroon else Color(0xFFDCC8A0)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .testTag("payment_method_${opt.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = opt.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) RoyalMaroon else Color(0xFF666666),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = opt.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp,
                                            color = if (isSelected) RoyalMaroon else Color(0xFF222222)
                                        )
                                        Text(
                                            text = opt.subtitle,
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF777777), fontSize = 10.5.sp)
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = RoyalMaroon, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Pay Button
                        Button(
                            onClick = {
                                isProcessing = true
                                onOrderPlaced(orderId, totalAmount, pointsRedeemed)
                                orderPlacedSuccess = true
                                isProcessing = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("place_order_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isProcessing && fullName.isNotBlank() && phone.isNotBlank() && address.isNotBlank()
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(color = RoyalGold, modifier = Modifier.size(20.dp))
                            } else {
                                Text(
                                    text = "Pay ₹$totalAmount & Place Order",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
