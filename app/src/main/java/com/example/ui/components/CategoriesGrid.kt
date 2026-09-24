package com.example.ui.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryItem
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.LightGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandSurface

@Composable
fun CategoriesGrid(
    categories: List<CategoryItem>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Royal Collections",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            if (selectedCategory != "all") {
                Text(
                    text = "Clear Filter",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = RoyalMaroon,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onCategorySelected("all") }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2x2 Grid or Row of Categories
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val displayCategories = categories.filter { it.id != "all" }
            displayCategories.take(2).forEach { category ->
                CategoryTile(
                    category = category,
                    isSelected = selectedCategory == category.id || selectedCategory == category.title,
                    onClick = {
                        onCategorySelected(if (selectedCategory == category.id) "all" else category.id)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val displayCategories = categories.filter { it.id != "all" }
            displayCategories.drop(2).forEach { category ->
                CategoryTile(
                    category = category,
                    isSelected = selectedCategory == category.id || selectedCategory == category.title,
                    onClick = {
                        onCategorySelected(if (selectedCategory == category.id) "all" else category.id)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CategoryTile(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColors = if (isSelected) {
        listOf(RoyalMaroon, DarkMaroon)
    } else {
        listOf(Color.White, SandSurface)
    }

    val borderColor = if (isSelected) RoyalGold else Color(0xFFE2C974).copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .height(88.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.verticalGradient(bgColors))
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("category_tile_${category.id}")
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) RoyalGold else MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    ),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = category.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color(0xFF666666),
                        fontSize = 10.5.sp,
                        lineHeight = 13.sp
                    ),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Mini artwork icon for category
            ProductArtwork(
                productType = when (category.id) {
                    "men" -> "Mojari"
                    "women" -> "Jutti"
                    "bridal" -> "Jutti"
                    else -> "Kolhapuri"
                },
                primaryColor = if (category.id == "women" || category.id == "bridal") "Maroon" else "Tan",
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(RoyalGold),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = DarkMaroon,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
