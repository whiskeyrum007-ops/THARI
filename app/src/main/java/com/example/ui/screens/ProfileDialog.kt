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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.local.RewardTransactionEntity
import com.example.ui.components.RoyalCrest
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandSurface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileDialog(
    rewardBalance: Int,
    rewardTransactions: List<RewardTransactionEntity>,
    onDismiss: () -> Unit,
    onTrackOrderClicked: (String?) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Royal Rewards, 1: Profile & Orders

    val tierName = when {
        rewardBalance >= 1500 -> "Maharaja Elite"
        rewardBalance >= 500 -> "Royal Patron"
        else -> "Darbar Novice"
    }

    val nextTierTarget = if (rewardBalance < 500) 500 else 1500
    val progress = (rewardBalance.toFloat() / nextTierTarget).coerceIn(0f, 1f)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold),
            modifier = Modifier.fillMaxWidth().testTag("profile_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Royal Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkMaroon)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = RoyalGold,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = DarkMaroon,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Padmashree Singh",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = "Verified", tint = RoyalGold, modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = "$tierName • Jaipur, RJ",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFF7E6C4), fontSize = 11.sp)
                                )
                            }
                        }

                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("profile_close_btn")) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                // Tabs for Royal Rewards & Profile details
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFF9F5EC),
                    contentColor = RoyalMaroon,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = RoyalMaroon
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Stars, contentDescription = null, tint = if (selectedTab == 0) RoyalMaroon else Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Royal Rewards", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.History, contentDescription = null, tint = if (selectedTab == 1) RoyalMaroon else Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Orders & Account", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    )
                }

                if (selectedTab == 0) {
                    // ROYAL REWARDS LOYALTY TAB
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Big Rewards Balance Card
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFFF9E8),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalGold),
                            shadowElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth().testTag("royal_rewards_balance_card")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Available Royal Points",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = Color(0xFF666666),
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "$rewardBalance",
                                                style = MaterialTheme.typography.headlineLarge.copy(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = RoyalMaroon
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Coins",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepGold
                                                )
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = CircleShape,
                                        color = RoyalMaroon,
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                Icons.Default.MonetizationOn,
                                                contentDescription = null,
                                                tint = RoyalGold,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE8F5E9),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "1 Coin = ₹1 Discount at Checkout (₹$rewardBalance Value)",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF1B5E20),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.5.sp
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Patron Tier & Progress
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Tier: $tierName",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = RoyalMaroon
                                    )
                                    Text(
                                        text = "$rewardBalance / $nextTierTarget pts",
                                        fontSize = 11.sp,
                                        color = Color(0xFF666666)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = RoyalMaroon,
                                    trackColor = Color(0xFFE0D4B8),
                                )

                                Text(
                                    text = if (rewardBalance >= 1500) "Top Tier Unlocked: Free Royal Muslin Pouch + 15% VIP Access"
                                    else "Earn ${nextTierTarget - rewardBalance} more coins to reach Maharaja Elite tier",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 10.5.sp,
                                        color = Color(0xFF777777)
                                    ),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // How to earn & redeem
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SandSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "How Royal Rewards Work",
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalMaroon,
                                    fontSize = 12.5.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "• Earn 10% back in coins on every handcrafted pair\n• Redeem directly on the Cart or Checkout screen for instant cash deduction\n• Never expires for active Rajasthan Heritage patrons",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF444444),
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Transaction Log
                        Text(
                            text = "Rewards Activity History",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoyalMaroon
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (rewardTransactions.isEmpty()) {
                            Text(
                                text = "No transactions yet. Start shopping to earn coins!",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        } else {
                            rewardTransactions.forEach { tx ->
                                val isPositive = tx.points >= 0
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFEADBBE)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = tx.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color(0xFF222222)
                                            )
                                            Text(
                                                text = tx.description,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF666666),
                                                    fontSize = 10.5.sp
                                                )
                                            )
                                        }

                                        Text(
                                            text = if (isPositive) "+${tx.points}" else "${tx.points}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = if (isPositive) Color(0xFF2E7D32) else RoyalMaroon
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // ORDERS & ACCOUNT TAB
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Default Delivery Address", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RoyalMaroon)
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SandSurface,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFDCC8A0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoyalMaroon, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Home • Johari Bazaar", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(
                                        "Haveli No. 4, Near Hawa Mahal, Jaipur, Rajasthan - 302001",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF555555), fontSize = 11.sp)
                                    )
                                    Text("Phone: +91 98290 12345", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF777777), fontSize = 10.5.sp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Recent Heritage Orders", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RoyalMaroon)
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFDCC8A0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Order #DR-92810", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Delivered ✓", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Royal Jodhpuri Gold Zari Mojari (Size 8)",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF444444), fontSize = 11.5.sp)
                                )
                                Text("Total: ₹2,499 • Earned 250 Royal Coins", style = MaterialTheme.typography.bodySmall.copy(color = DeepGold, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold))

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        onDismiss()
                                        onTrackOrderClicked("DR-92810")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("profile_track_order_btn")
                                ) {
                                    Text("🚚 Track Footwear Shipment", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Track Any Order
                        Surface(
                            onClick = {
                                onDismiss()
                                onTrackOrderClicked(null)
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF9EE),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2C974)),
                            modifier = Modifier.fillMaxWidth().testTag("profile_track_any_order_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Track Any Footwear Order", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = RoyalMaroon)
                                    Text("Have another order ID? View live courier status", fontSize = 10.5.sp, color = Color(0xFF666666))
                                }
                                Text("Track →", color = RoyalMaroon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Artisan Heritage Mission
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFBE8EC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2C974)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RoyalCrest(modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Preserving Marwar's Craft", fontWeight = FontWeight.Bold, color = RoyalMaroon, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Every purchase directly supports over 180 traditional leather craftsman families across Jodhpur, Jaipur, Barmer, and Bikaner.",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp, color = Color(0xFF444444), lineHeight = 15.sp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Close", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
