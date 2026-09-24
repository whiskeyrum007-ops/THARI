package com.example.ui.components

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import kotlinx.coroutines.delay

data class HeroSlide(
    val title: String,
    val subtitle: String,
    val tag: String,
    val productType: String,
    val colorTheme: String,
    val offerHighlight: String
)

@Composable
fun HeroBannerCarousel(
    onExploreClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val slides = remember {
        listOf(
            HeroSlide(
                title = "Handcrafted Royal Mojaris",
                subtitle = "Pure Silk Zari & Rawhide Soles by 4th-Gen Jodhpur Karigars",
                tag = "HERITAGE COUTURE",
                productType = "Mojari",
                colorTheme = "Maroon",
                offerHighlight = "Bite-Free Ortho Cushioning"
            ),
            HeroSlide(
                title = "Maharani Bridal Juttis",
                subtitle = "Intricate Antique Dabka, Kundan Stones & River Pearls",
                tag = "WEDDING ESSENTIALS",
                productType = "Jutti",
                colorTheme = "Maroon",
                offerHighlight = "Over 28 Hours Hand-Needlework"
            ),
            HeroSlide(
                title = "Marwari Braided Kolhapuris",
                subtitle = "Full-Grain Saddle Tan Leather Conditioned with Mustard Oil",
                tag = "TIMELESS RUSTIC",
                productType = "Kolhapuri",
                colorTheme = "Tan",
                offerHighlight = "Double Buff Leather Welt"
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }

    // Auto-advance banner every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % slides.size
        }
    }

    val currentSlide = slides[currentIndex]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        DarkMaroon,
                        RoyalMaroon,
                        Color(0xFF4A0012)
                    )
                )
            )
            .border(1.5.dp, RoyalGold, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Diamond,
                            contentDescription = null,
                            tint = RoyalGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentSlide.tag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = RoyalGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RoyalGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = currentSlide.offerHighlight,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFFFF0B8),
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentSlide.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 28.sp,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentSlide.subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onExploreClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalGold,
                            contentColor = DarkMaroon
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("explore_royal_collection_btn")
                    ) {
                        Text(
                            text = "Explore Royal Collection",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.3.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Interactive artwork preview thumbnail in carousel
                ProductArtwork(
                    productType = currentSlide.productType,
                    primaryColor = currentSlide.colorTheme,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Carousel Pager Dots & navigation arrows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    slides.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .width(if (index == currentIndex) 18.dp else 6.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (index == currentIndex) RoyalGold else Color.White.copy(alpha = 0.4f))
                                .clickable { currentIndex = index }
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = {
                            currentIndex = if (currentIndex > 0) currentIndex - 1 else slides.size - 1
                        },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Slide", tint = RoyalGold)
                    }
                    IconButton(
                        onClick = {
                            currentIndex = (currentIndex + 1) % slides.size
                        },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Next Slide", tint = RoyalGold)
                    }
                }
            }
        }
    }
}
