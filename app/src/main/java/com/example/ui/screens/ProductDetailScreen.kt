package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.SpecialDiscount
import com.example.ui.components.ProductArtwork
import com.example.ui.components.SizeGuideDialog
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandCream
import com.example.ui.theme.SandSurface

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    isWishlisted: Boolean,
    onBackClicked: () -> Unit,
    onWishlistToggle: () -> Unit,
    onAddToCart: (size: Int, quantity: Int, discount: SpecialDiscount) -> Unit,
    onBuyNow: (size: Int, discount: SpecialDiscount) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedAngle by remember { mutableStateOf("Front") }
    var selectedSize by remember { mutableIntStateOf(product.availableSizes.firstOrNull() ?: 7) }
    var selectedDiscount by remember { mutableStateOf(SpecialDiscount.NONE) }
    var verificationId by remember { mutableStateOf("") }
    var showSizeGuide by remember { mutableStateOf(false) }

    val angles = listOf("Front", "Side", "Sole", "Detail")

    val discountedPrice = if (selectedDiscount.percent > 0) {
        (product.price * (100 - selectedDiscount.percent)) / 100
    } else {
        product.price
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("product_detail_screen"),
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
                    IconButton(onClick = onBackClicked, modifier = Modifier.testTag("pdp_back_button")) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = RoyalGold
                        )
                    }

                    Text(
                        text = "Dharti Rajasthan Heritage",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalGold
                        )
                    )

                    IconButton(onClick = onWishlistToggle, modifier = Modifier.testTag("pdp_wishlist_button")) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) RoyalGold else Color.White
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Floating sticky bottom bar with Add to Cart & Buy Now
            Surface(
                color = Color.White,
                shadowElevation = 12.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(0.9f)) {
                        Text(
                            text = "₹$discountedPrice",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = RoyalMaroon
                            )
                        )
                        Text(
                            text = "Size $selectedSize (UK/IN)",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF666666))
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            onAddToCart(selectedSize, 1, selectedDiscount)
                        },
                        modifier = Modifier.weight(1.2f).height(46.dp).testTag("pdp_add_to_cart_btn"),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, RoyalMaroon)
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null, tint = RoyalMaroon, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add to Cart", color = RoyalMaroon, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            onBuyNow(selectedSize, selectedDiscount)
                        },
                        modifier = Modifier.weight(1.2f).height(46.dp).testTag("pdp_buy_now_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalMaroon)
                    ) {
                        Icon(Icons.Default.FlashOn, contentDescription = null, tint = RoyalGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Buy Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SandCream)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Artwork Gallery with Angle selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(16.dp)
            ) {
                ProductArtwork(
                    productType = product.type,
                    primaryColor = product.color,
                    angle = selectedAngle,
                    modifier = Modifier.fillMaxSize()
                )

                // Origin Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkMaroon.copy(alpha = 0.9f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = RoyalGold, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(product.artisanLocation, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Thumbnail Angle Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                angles.forEach { angle ->
                    val isSelected = selectedAngle == angle
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFFFFF6D8) else Color.White)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) RoyalMaroon else Color(0xFFDCC8A0),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedAngle = angle }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = when (angle) {
                                "Front" -> "Overview"
                                "Side" -> "Curved Nok"
                                "Sole" -> "Bite-Free Sole"
                                else -> "Embroidery"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) RoyalMaroon else Color(0xFF444444),
                                fontSize = 10.5.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Product Details Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 28.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.subtitle,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF666666))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rating & Reviews Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF2E7D32)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = String.format("%.2f", product.rating),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            }
                        }

                        Text(
                            text = "${product.reviewCount} Artisanal Ratings",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF555555))
                        )

                        Text("•", color = Color.Gray)

                        Text(
                            text = "Small Batch Handcrafted",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DeepGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Price and Discount
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "₹$discountedPrice",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = RoyalMaroon
                            )
                        )

                        if (product.originalPrice > discountedPrice) {
                            Text(
                                text = "₹${product.originalPrice}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.Gray,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = RoyalMaroon
                        ) {
                            Text(
                                text = "${product.discountPercent}% OFF",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = RoyalGold,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "Free Shipping across India on prepaid orders • Zero Shoe Bite Guarantee",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF2E7D32), fontSize = 11.5.sp),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    HorizontalDivider(color = Color(0xFFEADBBE), modifier = Modifier.padding(vertical = 14.dp))

                    // Interactive Size Selector & Size Guide Modal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Select Size (UK / Indian Standard)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { showSizeGuide = true }
                                .testTag("open_size_guide_btn")
                        ) {
                            Icon(Icons.Default.Straighten, contentDescription = null, tint = RoyalMaroon, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Size Guide",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = RoyalMaroon,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        product.availableSizes.forEach { sz ->
                            val isSelected = selectedSize == sz
                            Surface(
                                onClick = { selectedSize = sz },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) RoyalMaroon else Color.White,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) RoyalGold else Color(0xFFDCC8A0)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("size_chip_$sz")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = sz.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (isSelected) Color.White else Color(0xFF222222)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Special Discount Selector
                    Text(
                        text = "Apply Heritage / Institutional Concession",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SpecialDiscount.values().forEach { discount ->
                            val isSelected = selectedDiscount == discount
                            Surface(
                                onClick = { selectedDiscount = discount },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFFFFF7E6) else SandSurface,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) RoyalMaroon else Color(0xFFDCC8A0)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("discount_option_${discount.name}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = discount.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isSelected) RoyalMaroon else Color(0xFF222222)
                                        )
                                        if (discount.percent > 0) {
                                            Text(
                                                text = "Save ${discount.percent}% • Code: ${discount.code}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF2E7D32),
                                                    fontSize = 11.5.sp
                                                )
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = RoyalMaroon)
                                    }
                                }
                            }
                        }

                        // ID Verification prompt if required
                        if (selectedDiscount.idRequired) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold),
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = selectedDiscount.idPrompt,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = RoyalMaroon,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = verificationId,
                                        onValueChange = { verificationId = it },
                                        placeholder = { Text("e.g. ARMY-19842 / UNIV-2024-890", fontSize = 12.sp) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("discount_id_verification_input"),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Craftsmanship & Material Details
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADBBE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Authentic Craftsmanship & Specs",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalMaroon
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF333333),
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Spec items
                    SpecRow(title = "Sole Material", value = product.soleMaterial)
                    SpecRow(title = "Upper Material", value = product.upperMaterial)
                    SpecRow(title = "Origin & Karigars", value = product.artisanLocation)
                    SpecRow(title = "Care Instructions", value = product.careInstructions)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Key Features:",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    product.features.forEach { feat ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = RoyalGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(feat, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, color = Color(0xFF333333)))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showSizeGuide) {
        SizeGuideDialog(onDismiss = { showSizeGuide = false })
    }
}

@Composable
private fun SpecRow(title: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = RoyalMaroon,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF444444),
                lineHeight = 16.sp
            )
        )
        HorizontalDivider(color = Color(0xFFF0E5D0), modifier = Modifier.padding(top = 4.dp))
    }
}
