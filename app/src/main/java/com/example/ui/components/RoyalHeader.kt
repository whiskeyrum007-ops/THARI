package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandCream
import com.example.ui.theme.SandSurface

@Composable
fun RoyalHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    wishlistCount: Int,
    cartCount: Int,
    royalPoints: Int = 0,
    onWishlistClicked: () -> Unit,
    onCartClicked: () -> Unit,
    onTrackOrderClicked: () -> Unit = {},
    onProfileClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkMaroon,
                        RoyalMaroon
                    )
                )
            )
            .padding(top = 8.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
    ) {
        // Top Branding Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { /* Reset to home if needed */ }
            ) {
                RoyalCrest(modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Dharti Rajasthan",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = RoyalGold,
                            letterSpacing = 0.8.sp,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "ROYAL HANDCRAFTED FOOTWEAR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFF7E6C4),
                            letterSpacing = 1.8.sp,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Action Icons (Wishlist, Cart, Profile)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Wishlist with dynamic badge counter from local storage
                BadgedBox(
                    badge = {
                        if (wishlistCount > 0) {
                            Badge(
                                containerColor = RoyalGold,
                                contentColor = DarkMaroon,
                                modifier = Modifier.testTag("wishlist_badge_counter")
                            ) {
                                Text(
                                    text = wishlistCount.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onWishlistClicked,
                        modifier = Modifier.testTag("wishlist_icon_button")
                    ) {
                        Icon(
                            imageVector = if (wishlistCount > 0) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist with $wishlistCount saved items",
                            tint = if (wishlistCount > 0) RoyalGold else Color.White
                        )
                    }
                }

                // Cart with dynamic badge counter from local storage
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = RoyalGold,
                                contentColor = DarkMaroon,
                                modifier = Modifier.testTag("cart_badge_counter")
                            ) {
                                Text(
                                    text = cartCount.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onCartClicked,
                        modifier = Modifier.testTag("cart_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Cart with $cartCount items",
                            tint = Color.White
                        )
                    }
                }

                // Track My Order
                IconButton(
                    onClick = onTrackOrderClicked,
                    modifier = Modifier.testTag("header_track_order_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Track My Order",
                        tint = RoyalGold,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Royal Rewards Points Chip (opens Profile / Rewards hub)
                Surface(
                    onClick = onProfileClicked,
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                    modifier = Modifier.testTag("header_royal_points_chip")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "👑",
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "$royalPoints",
                            fontWeight = FontWeight.Bold,
                            color = RoyalGold,
                            fontSize = 11.sp
                        )
                    }
                }

                // Profile
                IconButton(
                    onClick = onProfileClicked,
                    modifier = Modifier.testTag("user_profile_icon_button")
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f),
                        modifier = Modifier.size(32.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Profile",
                            tint = Color.White,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar & Filter Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = {
                    Text(
                        "Search Mojaris, Juttis, Kolhapuris...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 13.sp
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = RoyalGold,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.12f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = RoyalGold.copy(alpha = 0.5f),
                    cursorColor = RoyalGold
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("search_bar_input")
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Filter & Sort quick button
            Surface(
                onClick = onFilterClicked,
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                modifier = Modifier
                    .size(50.dp)
                    .testTag("filter_sort_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = "Filter & Sort footwear",
                        tint = RoyalGold,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
