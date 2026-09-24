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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CartEntity
import com.example.data.model.SpecialDiscount
import com.example.ui.components.ProductArtwork
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandCream
import com.example.ui.theme.SandSurface
import kotlin.math.min

@Composable
fun CartScreen(
    cartItems: List<CartEntity>,
    rewardBalance: Int,
    redeemedPoints: Int,
    onRedeemPoints: (Int) -> Unit,
    onBackClicked: () -> Unit,
    onUpdateQuantity: (id: Int, newQuantity: Int) -> Unit,
    onRemoveItem: (id: Int) -> Unit,
    onProceedToCheckout: () -> Unit,
    onExploreClicked: () -> Unit,
    activeDiscount: SpecialDiscount,
    onSelectDiscount: (SpecialDiscount) -> Unit,
    modifier: Modifier = Modifier
) {
    var pincodeInput by remember { mutableStateOf("302001") }
    var pincodeStatus by remember { mutableStateOf<String?>("Delivering to Jaipur: 3-4 days (Express Handcrafted Dispatch)") }
    var idVerificationText by remember { mutableStateOf("") }

    val rawSubtotal = cartItems.sumOf { it.price * it.quantity }
    val discountPercent = activeDiscount.percent
    val discountSavings = (rawSubtotal * discountPercent) / 100
    val deliveryFee = if (rawSubtotal > 999 || rawSubtotal == 0) 0 else 99

    // Subtotal after percentage discount
    val afterCouponSubtotal = rawSubtotal - discountSavings

    // Safe redeemed points capped at available balance and remaining subtotal
    val actualRedeemedDiscount = min(redeemedPoints, min(rewardBalance, afterCouponSubtotal)).coerceAtLeast(0)

    val finalTotal = (afterCouponSubtotal - actualRedeemedDiscount + deliveryFee).coerceAtLeast(0)
    val pointsToEarn = ((finalTotal * 10) / 100).coerceAtLeast(25)

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("cart_screen"),
        topBar = {
            Surface(
                color = DarkMaroon,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClicked, modifier = Modifier.testTag("cart_back_button")) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = RoyalGold
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Shopping Bag (${cartItems.sumOf { it.quantity }})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoyalGold
                            )
                        )
                    }

                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = RoyalGold,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    color = Color.White,
                    shadowElevation = 12.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Payable",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF666666))
                            )
                            Text(
                                text = "₹$finalTotal",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RoyalMaroon
                                )
                            )
                            val totalSaved = discountSavings + actualRedeemedDiscount
                            if (totalSaved > 0) {
                                Text(
                                    text = "Total savings: ₹$totalSaved",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = onProceedToCheckout,
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .height(46.dp)
                                .testTag("cart_checkout_button")
                        ) {
                            Text(
                                text = "Proceed to Checkout",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SandCream)
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0xFFFFF7E0),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                        modifier = Modifier.size(90.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = RoyalMaroon,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Your Shopping Bag is Empty",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalMaroon
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Discover pure silk zari Mojaris and embroidered Juttis crafted by 4th generation karigars.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onExploreClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("cart_explore_btn")
                    ) {
                        Text("Explore Royal Footwear", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SandCream)
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Free Shipping & Rewards Banner
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFE8F5E9),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFF2E7D32)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Stars,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Free Express Shipping unlocked! Plus earn ~$pointsToEarn Royal Coins on this order.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF1B5E20),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                // Cart Items List
                items(cartItems, key = { it.id }) { item ->
                    CartItemCard(
                        item = item,
                        onIncrement = { onUpdateQuantity(item.id, item.quantity + 1) },
                        onDecrement = { onUpdateQuantity(item.id, item.quantity - 1) },
                        onRemove = { onRemoveItem(item.id) }
                    )
                }

                // ROYAL REWARDS REDEMPTION SECTION
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFFFF9E8),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold),
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth().testTag("cart_royal_rewards_section")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = RoyalMaroon,
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.MonetizationOn,
                                                contentDescription = null,
                                                tint = RoyalGold,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Redeem Royal Rewards",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalMaroon
                                        )
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, RoyalGold)
                                ) {
                                    Text(
                                        text = "$rewardBalance Coins Available",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = DeepGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Use your earned Shahi Coins for an instant cash discount (1 Coin = ₹1).",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF555555), fontSize = 11.5.sp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick redemption choices
                            val maxRedeemable = min(rewardBalance, afterCouponSubtotal)
                            val options = listOf(
                                0 to "None",
                                100 to "100 pts (-₹100)",
                                250 to "250 pts (-₹250)",
                                maxRedeemable to "Max ($maxRedeemable pts)"
                            ).filter { it.first <= maxRedeemable || it.first == 0 }.distinctBy { it.first }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                options.forEach { (pts, label) ->
                                    val isSelected = actualRedeemedDiscount == pts
                                    Surface(
                                        onClick = { onRedeemPoints(pts) },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) RoyalMaroon else Color.White,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) RoyalGold else Color(0xFFDCC8A0)
                                        ),
                                        modifier = Modifier.weight(1f).height(38.dp).testTag("redeem_points_btn_$pts")
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = label,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else Color(0xFF333333),
                                                textAlign = TextAlign.Center,
                                                lineHeight = 12.sp,
                                                modifier = Modifier.padding(horizontal = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            if (actualRedeemedDiscount > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "✓ ₹$actualRedeemedDiscount Royal Discount will be deducted from your total order!",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Pincode Delivery Checker
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PinDrop, contentDescription = null, tint = RoyalMaroon, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Delivery Pincode Checker",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = pincodeInput,
                                    onValueChange = {
                                        if (it.length <= 6) pincodeInput = it
                                    },
                                    placeholder = { Text("Enter 6-digit Pincode", fontSize = 12.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).height(50.dp).testTag("cart_pincode_input"),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        pincodeStatus = when {
                                            pincodeInput.startsWith("30") -> "Delivering to Rajasthan: 2-3 days (Express Handcrafted Dispatch)"
                                            pincodeInput.startsWith("11") -> "Delivering to Delhi NCR: 3-4 days"
                                            pincodeInput.startsWith("40") -> "Delivering to Mumbai: 3-4 days"
                                            pincodeInput.startsWith("56") -> "Delivering to Bangalore: 4-5 days"
                                            pincodeInput.length == 6 -> "Delivering across India: 4-6 days (Free Handcrafted Delivery)"
                                            else -> "Please enter a valid 6-digit Pincode"
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                                    modifier = Modifier.height(50.dp).testTag("cart_pincode_check_btn")
                                ) {
                                    Text("Check", fontWeight = FontWeight.Bold)
                                }
                            }

                            if (pincodeStatus != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = pincodeStatus!!,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (pincodeStatus!!.contains("valid")) Color.Red else Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Special Discount & ID Verification
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Institutional / Heritage Concessions",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalMaroon
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            SpecialDiscount.values().filter { it != SpecialDiscount.NONE }.forEach { disc ->
                                val isSelected = activeDiscount == disc
                                Surface(
                                    onClick = {
                                        onSelectDiscount(if (isSelected) SpecialDiscount.NONE else disc)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) Color(0xFFFFF7E6) else SandSurface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) RoyalMaroon else Color(0xFFDCC8A0)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .testTag("cart_discount_option_${disc.name}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "${disc.title} (${disc.percent}% OFF)",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp,
                                                color = if (isSelected) RoyalMaroon else Color(0xFF222222)
                                            )
                                            Text(
                                                text = "Coupon Code: ${disc.code}",
                                                style = MaterialTheme.typography.labelSmall.copy(color = DeepGold)
                                            )
                                        }

                                        if (isSelected) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = RoyalMaroon)
                                        }
                                    }
                                }
                            }

                            if (activeDiscount.idRequired) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "ID Proof Verification for ${activeDiscount.code}:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalMaroon
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = idVerificationText,
                                    onValueChange = { idVerificationText = it },
                                    placeholder = { Text("Enter ID / Roll / Service Number", fontSize = 11.5.sp) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("cart_id_verification_input"),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                Text(
                                    text = "Will be validated upon package dispatch",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 10.sp),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Price Breakdown Bill Details
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Price Details",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            BillRow("Bag Total", "₹$rawSubtotal")

                            if (discountSavings > 0) {
                                BillRow(
                                    label = "Special Heritage Concession (${activeDiscount.percent}%)",
                                    value = "-₹$discountSavings",
                                    valueColor = Color(0xFF2E7D32)
                                )
                            }

                            if (actualRedeemedDiscount > 0) {
                                BillRow(
                                    label = "Royal Rewards Coins Redeemed",
                                    value = "-₹$actualRedeemedDiscount",
                                    valueColor = Color(0xFF2E7D32)
                                )
                            }

                            BillRow(
                                label = "Express Delivery",
                                value = if (deliveryFee == 0) "FREE" else "₹$deliveryFee",
                                valueColor = Color(0xFF2E7D32)
                            )

                            HorizontalDivider(color = Color(0xFFEADBBE), modifier = Modifier.padding(vertical = 8.dp))

                            BillRow(
                                label = "Total Payable Amount",
                                value = "₹$finalTotal",
                                isBold = true,
                                valueColor = RoyalMaroon
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFFF9E8)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = DeepGold, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "You will earn +$pointsToEarn Royal Coins with this order",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = DeepGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.5.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartEntity,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE)),
        shadowElevation = 1.5.dp,
        modifier = Modifier.fillMaxWidth().testTag("cart_item_${item.id}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductArtwork(
                productType = if (item.name.contains("Jutti", ignoreCase = true)) "Jutti" else "Mojari",
                primaryColor = item.color,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Size: UK/IN ${item.size}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666), fontSize = 11.5.sp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${item.price * item.quantity}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = RoyalMaroon
                        )
                    )

                    // Quantity increment/decrement
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SandSurface)
                            .border(0.5.dp, Color(0xFFDCC8A0), RoundedCornerShape(6.dp))
                    ) {
                        IconButton(onClick = onDecrement, modifier = Modifier.size(28.dp).testTag("qty_decrease_${item.id}")) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = item.quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        IconButton(onClick = onIncrement, modifier = Modifier.size(28.dp).testTag("qty_increase_${item.id}")) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                        }
                    }

                    IconButton(onClick = onRemove, modifier = Modifier.size(28.dp).testTag("cart_remove_item_${item.id}")) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Remove item", tint = Color.Gray, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BillRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    valueColor: Color = Color(0xFF222222)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isBold) MaterialTheme.colorScheme.onSurface else Color(0xFF555555),
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (isBold) 14.sp else 13.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = valueColor,
                fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.SemiBold,
                fontSize = if (isBold) 15.sp else 13.sp
            )
        )
    }
}
