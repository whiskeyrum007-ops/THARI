package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductCatalog
import com.example.data.local.WishlistEntity
import com.example.data.model.Product
import com.example.data.model.SpecialDiscount
import com.example.ui.components.CategoriesGrid
import com.example.ui.components.CraftsmanshipTrustBadges
import com.example.ui.components.FilterSortSheet
import com.example.ui.components.FilterState
import com.example.ui.components.HeroBannerCarousel
import com.example.ui.components.ProductCard
import com.example.ui.components.RoyalHeader
import com.example.ui.components.SpecialOffersBar
import com.example.ui.components.WhatsAppFloatingButton
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandCream

@Composable
fun HomeScreen(
    products: List<Product>,
    wishlistItems: List<WishlistEntity>,
    wishlistCount: Int,
    cartCount: Int,
    royalPoints: Int = 0,
    searchQuery: String,
    selectedCategory: String,
    filterState: FilterState,
    activeDiscount: SpecialDiscount,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onFilterApplied: (FilterState) -> Unit,
    onSelectDiscount: (SpecialDiscount) -> Unit,
    onProductClicked: (Product) -> Unit,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product, Int) -> Unit,
    onNavigateWishlist: () -> Unit,
    onNavigateCart: () -> Unit,
    onOpenProfile: () -> Unit,
    onTrackOrderClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    val wishlistedIds = remember(wishlistItems) {
        wishlistItems.map { it.productId }.toSet()
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("home_screen"),
        topBar = {
            RoyalHeader(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                wishlistCount = wishlistCount,
                cartCount = cartCount,
                royalPoints = royalPoints,
                onWishlistClicked = onNavigateWishlist,
                onCartClicked = onNavigateCart,
                onTrackOrderClicked = onTrackOrderClicked,
                onProfileClicked = onOpenProfile,
                onFilterClicked = { showFilterSheet = true }
            )
        },
        floatingActionButton = {
            WhatsAppFloatingButton()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SandCream)
                .padding(innerPadding)
        ) {
            // Hero Banner Carousel
            item {
                HeroBannerCarousel(
                    onExploreClicked = {
                        onCategorySelected("all")
                    }
                )
            }

            // Categories Grid
            item {
                CategoriesGrid(
                    categories = ProductCatalog.categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            // Special Offers Bar (Rajasthan, Military, Student)
            item {
                SpecialOffersBar(
                    onSelectOffer = onSelectDiscount,
                    activeOffer = activeDiscount
                )
            }

            // Craftsmanship Trust Badges
            item {
                CraftsmanshipTrustBadges()
            }

            // Quick Track Footwear Order Banner
            item {
                Surface(
                    onClick = onTrackOrderClicked,
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF9EE),
                    border = BorderStroke(1.dp, Color(0xFFE2C974)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("home_track_order_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = com.example.ui.theme.RoyalMaroon,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.LocalShipping,
                                        contentDescription = null,
                                        tint = com.example.ui.theme.RoyalGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Track My Handcrafted Footwear 🚚",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = com.example.ui.theme.RoyalMaroon
                                )
                                Text(
                                    text = "Live Marwar artisan handcraft & air courier status",
                                    fontSize = 11.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Track",
                            tint = com.example.ui.theme.RoyalMaroon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Section Title: Featured Handcrafted Products
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Diamond, contentDescription = null, tint = RoyalMaroon, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "Search Results (${products.size})" else "Artisan Footwear (${products.size})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    if (filterState.footwearType != "All" || filterState.gender != "All" || filterState.size != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFBE8EC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RoyalMaroon),
                            modifier = Modifier.clickable { onFilterApplied(FilterState()) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Reset Filters", fontSize = 10.sp, color = RoyalMaroon, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(Icons.Default.Close, contentDescription = null, tint = RoyalMaroon, modifier = Modifier.size(12.dp))
                            }
                        }
                    }
                }
            }

            // Product Cards Grid
            if (products.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = RoyalMaroon,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No Footwear Found",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Try adjusting your search terms or filters.",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, textAlign = TextAlign.Center)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onSearchQueryChanged("")
                                    onCategorySelected("all")
                                    onFilterApplied(FilterState())
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon)
                            ) {
                                Text("Show All Collections", color = Color.White)
                            }
                        }
                    }
                }
            } else {
                items(products, key = { it.id }) { product ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        ProductCard(
                            product = product,
                            isWishlisted = wishlistedIds.contains(product.id),
                            onWishlistToggle = { onWishlistToggle(product) },
                            onProductClicked = { onProductClicked(product) },
                            onAddToCartClicked = { size -> onAddToCart(product, size) }
                        )
                    }
                }
            }

            // Bottom Spacing for Floating buttons
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

    if (showFilterSheet) {
        FilterSortSheet(
            currentFilter = filterState,
            onApplyFilter = onFilterApplied,
            onDismiss = { showFilterSheet = false }
        )
    }
}
