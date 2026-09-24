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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SpecialDiscount
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon

data class OfferCardData(
    val discount: SpecialDiscount,
    val headline: String,
    val icon: ImageVector,
    val colorStart: Color,
    val colorEnd: Color
)

@Composable
fun SpecialOffersBar(
    onSelectOffer: (SpecialDiscount) -> Unit,
    activeOffer: SpecialDiscount,
    modifier: Modifier = Modifier
) {
    val offers = listOf(
        OfferCardData(
            discount = SpecialDiscount.RAJASTHAN,
            headline = "15% OFF for Rajasthan addresses",
            icon = Icons.Default.CardGiftcard,
            colorStart = RoyalMaroon,
            colorEnd = Color(0xFF5A0016)
        ),
        OfferCardData(
            discount = SpecialDiscount.MILITARY,
            headline = "20% OFF Armed Forces & Veterans",
            icon = Icons.Default.MilitaryTech,
            colorStart = Color(0xFF1E5128),
            colorEnd = Color(0xFF113217)
        ),
        OfferCardData(
            discount = SpecialDiscount.STUDENT,
            headline = "18% OFF College / University Students",
            icon = Icons.Default.School,
            colorStart = Color(0xFF2B3A67),
            colorEnd = Color(0xFF161F38)
        )
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Special Heritage Concessions",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            Text(
                text = "Tap to apply",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = RoyalGold,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(offers) { item ->
                val isApplied = activeOffer == item.discount

                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(item.colorStart, item.colorEnd)
                            )
                        )
                        .border(
                            width = if (isApplied) 2.dp else 1.dp,
                            color = if (isApplied) RoyalGold else RoyalGold.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectOffer(item.discount) }
                        .testTag("offer_card_${item.discount.code}")
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = RoyalGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = item.discount.title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1
                                )
                            }

                            if (isApplied) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Applied",
                                    tint = RoyalGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.headline,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.5.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, RoyalGold.copy(alpha = 0.7f))
                            ) {
                                Text(
                                    text = item.discount.code,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = RoyalGold,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Text(
                                text = if (isApplied) "Applied ✓" else "Apply Discount",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isApplied) RoyalGold else Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
