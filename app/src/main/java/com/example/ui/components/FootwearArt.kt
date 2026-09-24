package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkMaroon
import com.example.ui.theme.DeepGold
import com.example.ui.theme.LightGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalMaroon
import com.example.ui.theme.SandCream
import com.example.ui.theme.SandSurface

@Composable
fun ProductArtwork(
    productType: String,
    primaryColor: String,
    modifier: Modifier = Modifier,
    angle: String = "Front" // "Front", "Side", "Sole", "Detail"
) {
    val bgBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFFFDF9),
            Color(0xFFF7F1E5),
            Color(0xFFEFE6D5)
        )
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgBrush)
            .border(1.dp, Color(0xFFE2C974).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            val w = size.width
            val h = size.height

            // Subtle gold ornamental background halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(RoyalGold.copy(alpha = 0.25f), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.5f),
                    radius = w * 0.45f
                ),
                radius = w * 0.45f,
                center = Offset(w * 0.5f, h * 0.5f)
            )

            // Ornamental mandala corners
            drawCornerOrnament(w, h)

            when (angle) {
                "Sole" -> drawSoleIllustration(w, h)
                "Detail" -> drawEmbroideryDetailIllustration(w, h, primaryColor)
                "Side" -> drawSideFootwearIllustration(w, h, productType, primaryColor)
                else -> drawMainFootwearIllustration(w, h, productType, primaryColor)
            }
        }
    }
}

private fun DrawScope.drawCornerOrnament(w: Float, h: Float) {
    val gold = RoyalGold.copy(alpha = 0.4f)
    val r = 16f
    // Top-left
    drawLine(gold, Offset(12f, 12f), Offset(12f + r, 12f), strokeWidth = 2f)
    drawLine(gold, Offset(12f, 12f), Offset(12f, 12f + r), strokeWidth = 2f)
    // Top-right
    drawLine(gold, Offset(w - 12f, 12f), Offset(w - 12f - r, 12f), strokeWidth = 2f)
    drawLine(gold, Offset(w - 12f, 12f), Offset(w - 12f, 12f + r), strokeWidth = 2f)
    // Bottom-left
    drawLine(gold, Offset(12f, h - 12f), Offset(12f + r, h - 12f), strokeWidth = 2f)
    drawLine(gold, Offset(12f, h - 12f), Offset(12f, h - 12f - r), strokeWidth = 2f)
    // Bottom-right
    drawLine(gold, Offset(w - 12f, h - 12f), Offset(w - 12f - r, h - 12f), strokeWidth = 2f)
    drawLine(gold, Offset(w - 12f, h - 12f), Offset(w - 12f, h - 12f - r), strokeWidth = 2f)
}

private fun DrawScope.drawMainFootwearIllustration(
    w: Float,
    h: Float,
    type: String,
    colorTheme: String
) {
    val mainColor = when {
        colorTheme.contains("Maroon", ignoreCase = true) -> RoyalMaroon
        colorTheme.contains("Green", ignoreCase = true) -> Color(0xFF1B4D3E)
        colorTheme.contains("Black", ignoreCase = true) -> Color(0xFF222222)
        colorTheme.contains("Gold", ignoreCase = true) -> Color(0xFFC59B27)
        colorTheme.contains("Tan", ignoreCase = true) -> Color(0xFF8B5A2B)
        else -> RoyalMaroon
    }

    when (type) {
        "Kolhapuri" -> drawKolhapuriArt(w, h)
        "Jutti" -> drawJuttiArt(w, h, mainColor)
        "Slip-on" -> drawSliponArt(w, h)
        else -> drawMojariArt(w, h, mainColor)
    }
}

private fun DrawScope.drawMojariArt(w: Float, h: Float, baseColor: Color) {
    // Drop shadow
    val shadowPath = Path().apply {
        moveTo(w * 0.18f, h * 0.72f)
        cubicTo(w * 0.35f, h * 0.85f, w * 0.65f, h * 0.85f, w * 0.82f, h * 0.72f)
        cubicTo(w * 0.65f, h * 0.76f, w * 0.35f, h * 0.76f, w * 0.18f, h * 0.72f)
    }
    drawPath(shadowPath, Color(0x33000000))

    // Leather Sole Base
    val solePath = Path().apply {
        moveTo(w * 0.22f, h * 0.62f)
        cubicTo(w * 0.2f, h * 0.40f, w * 0.30f, h * 0.22f, w * 0.50f, h * 0.18f)
        // Characteristic curved pointy Nok
        cubicTo(w * 0.52f, h * 0.14f, w * 0.55f, h * 0.12f, w * 0.58f, h * 0.10f)
        cubicTo(w * 0.56f, h * 0.16f, w * 0.60f, h * 0.22f, w * 0.70f, h * 0.30f)
        cubicTo(w * 0.80f, h * 0.42f, w * 0.78f, h * 0.62f, w * 0.74f, h * 0.70f)
        cubicTo(w * 0.68f, h * 0.76f, w * 0.32f, h * 0.76f, w * 0.22f, h * 0.62f)
    }
    drawPath(solePath, Color(0xFFC49A45))
    drawPath(solePath, DeepGold, style = Stroke(width = 3f))

    // Upper Fabric Body
    val upperPath = Path().apply {
        moveTo(w * 0.25f, h * 0.60f)
        cubicTo(w * 0.24f, h * 0.42f, w * 0.32f, h * 0.26f, w * 0.50f, h * 0.22f)
        cubicTo(w * 0.52f, h * 0.17f, w * 0.55f, h * 0.15f, w * 0.57f, h * 0.13f)
        cubicTo(w * 0.55f, h * 0.19f, w * 0.58f, h * 0.24f, w * 0.66f, h * 0.32f)
        cubicTo(w * 0.75f, h * 0.43f, w * 0.74f, h * 0.60f, w * 0.70f, h * 0.67f)
        // Inner foot opening
        cubicTo(w * 0.62f, h * 0.55f, w * 0.38f, h * 0.55f, w * 0.25f, h * 0.60f)
    }
    drawPath(
        upperPath,
        Brush.linearGradient(
            listOf(baseColor, DarkMaroon),
            start = Offset(w * 0.3f, h * 0.2f),
            end = Offset(w * 0.7f, h * 0.7f)
        )
    )

    // Inner insole with bite-free cushioning cushion
    val insolePath = Path().apply {
        moveTo(w * 0.32f, h * 0.58f)
        cubicTo(w * 0.38f, h * 0.54f, w * 0.62f, h * 0.54f, w * 0.68f, h * 0.58f)
        cubicTo(w * 0.66f, h * 0.68f, w * 0.34f, h * 0.68f, w * 0.32f, h * 0.58f)
    }
    drawPath(insolePath, Color(0xFFF7E6C4))
    drawPath(insolePath, RoyalGold, style = Stroke(width = 1.5f))

    // Zari Gold Embroidery Lattice on upper
    val goldStroke = Stroke(width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    // Central crest line
    drawLine(RoyalGold, Offset(w * 0.50f, h * 0.23f), Offset(w * 0.50f, h * 0.54f), strokeWidth = 2.5f)

    // Chevron Zari Vines
    for (i in 1..4) {
        val yOffset = h * 0.26f + (i * h * 0.055f)
        drawLine(RoyalGold, Offset(w * 0.50f, yOffset), Offset(w * 0.38f + (i * 4f), yOffset + 14f), strokeWidth = 1.8f)
        drawLine(RoyalGold, Offset(w * 0.50f, yOffset), Offset(w * 0.62f - (i * 4f), yOffset + 14f), strokeWidth = 1.8f)
        // Little moti/pearl dots
        drawCircle(Color.White, radius = 2.8f, center = Offset(w * 0.38f + (i * 4f), yOffset + 14f))
        drawCircle(RoyalGold, radius = 1.5f, center = Offset(w * 0.38f + (i * 4f), yOffset + 14f))
        drawCircle(Color.White, radius = 2.8f, center = Offset(w * 0.62f - (i * 4f), yOffset + 14f))
        drawCircle(RoyalGold, radius = 1.5f, center = Offset(w * 0.62f - (i * 4f), yOffset + 14f))
    }

    // Gold trim around foot opening
    val rimPath = Path().apply {
        moveTo(w * 0.25f, h * 0.60f)
        cubicTo(w * 0.38f, h * 0.55f, w * 0.62f, h * 0.55f, w * 0.70f, h * 0.60f)
    }
    drawPath(rimPath, RoyalGold, style = Stroke(width = 4f))

    // Pointed Nok tip accent
    drawCircle(RoyalGold, radius = 4f, center = Offset(w * 0.57f, h * 0.13f))
    drawCircle(Color(0xFFFFF6D8), radius = 2f, center = Offset(w * 0.57f, h * 0.13f))
}

private fun DrawScope.drawJuttiArt(w: Float, h: Float, baseColor: Color) {
    // Drop shadow
    val shadowPath = Path().apply {
        moveTo(w * 0.2f, h * 0.74f)
        cubicTo(w * 0.35f, h * 0.84f, w * 0.65f, h * 0.84f, w * 0.8f, h * 0.74f)
        cubicTo(w * 0.65f, h * 0.77f, w * 0.35f, h * 0.77f, w * 0.2f, h * 0.74f)
    }
    drawPath(shadowPath, Color(0x30000000))

    // Elegant curved silhouette of Jutti
    val solePath = Path().apply {
        moveTo(w * 0.24f, h * 0.65f)
        cubicTo(w * 0.20f, h * 0.42f, w * 0.32f, h * 0.22f, w * 0.50f, h * 0.18f)
        cubicTo(w * 0.68f, h * 0.22f, w * 0.80f, h * 0.42f, w * 0.76f, h * 0.65f)
        cubicTo(w * 0.72f, h * 0.75f, w * 0.28f, h * 0.75f, w * 0.24f, h * 0.65f)
    }
    drawPath(solePath, Color(0xFFD4B170))
    drawPath(solePath, DeepGold, style = Stroke(width = 2.5f))

    // Velvet Upper
    val upperPath = Path().apply {
        moveTo(w * 0.25f, h * 0.62f)
        cubicTo(w * 0.22f, h * 0.43f, w * 0.33f, h * 0.24f, w * 0.50f, h * 0.20f)
        cubicTo(w * 0.67f, h * 0.24f, w * 0.78f, h * 0.43f, w * 0.75f, h * 0.62f)
        // Throat line
        cubicTo(w * 0.65f, h * 0.52f, w * 0.35f, h * 0.52f, w * 0.25f, h * 0.62f)
    }
    drawPath(
        upperPath,
        Brush.radialGradient(
            listOf(baseColor, baseColor.copy(alpha = 0.85f), DarkMaroon),
            center = Offset(w * 0.5f, h * 0.38f),
            radius = w * 0.35f
        )
    )

    // Inner cushioned sole
    val innerPath = Path().apply {
        moveTo(w * 0.30f, h * 0.57f)
        cubicTo(w * 0.38f, h * 0.52f, w * 0.62f, h * 0.52f, w * 0.70f, h * 0.57f)
        cubicTo(w * 0.68f, h * 0.70f, w * 0.32f, h * 0.70f, w * 0.30f, h * 0.57f)
    }
    drawPath(innerPath, Color(0xFFFBEBC7))

    // Dabka and Pearl Flower Bouquet at the toe center
    val flowerCenter = Offset(w * 0.50f, h * 0.33f)
    drawCircle(RoyalGold, radius = 7f, center = flowerCenter)
    drawCircle(Color.White, radius = 4f, center = flowerCenter)

    // 6 Petals of Kundan / Pearl
    for (i in 0 until 6) {
        val angleRad = (i * 60) * (Math.PI / 180.0)
        val px = flowerCenter.x + (Math.cos(angleRad) * 16f).toFloat()
        val py = flowerCenter.y + (Math.sin(angleRad) * 16f).toFloat()
        drawCircle(Color(0xFFFFFDF5), radius = 4.2f, center = Offset(px, py))
        drawCircle(RoyalGold, radius = 4.2f, center = Offset(px, py), style = Stroke(width = 1.2f))
    }

    // Gold Paisley Vines around toe
    for (step in 1..3) {
        val arcY = h * 0.40f + (step * 14f)
        drawArc(
            color = RoyalGold,
            startAngle = 160f,
            sweepAngle = 220f,
            useCenter = false,
            topLeft = Offset(w * 0.35f, arcY),
            size = Size(w * 0.30f, 20f),
            style = Stroke(width = 1.8f)
        )
    }

    // Gold Gota Patti trim along rim
    val rim = Path().apply {
        moveTo(w * 0.25f, h * 0.62f)
        cubicTo(w * 0.35f, h * 0.52f, w * 0.65f, h * 0.52f, w * 0.75f, h * 0.62f)
    }
    drawPath(rim, RoyalGold, style = Stroke(width = 3.5f))
}

private fun DrawScope.drawKolhapuriArt(w: Float, h: Float) {
    val leatherTan = Color(0xFF9E6233)
    val darkTan = Color(0xFF6E3B13)
    val lightTan = Color(0xFFCA986D)

    // Sole outline
    val sole = Path().apply {
        moveTo(w * 0.22f, h * 0.68f)
        cubicTo(w * 0.18f, h * 0.45f, w * 0.28f, h * 0.25f, w * 0.50f, h * 0.20f)
        cubicTo(w * 0.72f, h * 0.25f, w * 0.82f, h * 0.45f, w * 0.78f, h * 0.68f)
        cubicTo(w * 0.72f, h * 0.78f, w * 0.28f, h * 0.78f, w * 0.22f, h * 0.68f)
    }
    drawPath(sole, leatherTan)
    drawPath(sole, darkTan, style = Stroke(width = 3f))

    // Welt stitching detail
    drawPath(
        sole,
        RoyalGold.copy(alpha = 0.8f),
        style = Stroke(width = 1.5f, cap = StrokeCap.Round)
    )

    // Toe loop
    drawCircle(darkTan, radius = 14f, center = Offset(w * 0.42f, h * 0.30f), style = Stroke(width = 5f))
    drawCircle(lightTan, radius = 10f, center = Offset(w * 0.42f, h * 0.30f), style = Stroke(width = 2.5f))

    // Hand-Braided Center Plait
    val braidStartY = h * 0.34f
    val braidEndY = h * 0.62f
    for (i in 0..6) {
        val y = braidStartY + (i * 12f)
        drawLine(darkTan, Offset(w * 0.45f, y), Offset(w * 0.55f, y + 8f), strokeWidth = 5f, cap = StrokeCap.Round)
        drawLine(lightTan, Offset(w * 0.55f, y), Offset(w * 0.45f, y + 8f), strokeWidth = 4f, cap = StrokeCap.Round)
        drawLine(RoyalGold, Offset(w * 0.45f, y), Offset(w * 0.55f, y + 8f), strokeWidth = 1.5f)
    }

    // Side Straps
    val strap1 = Path().apply {
        moveTo(w * 0.24f, h * 0.52f)
        lineTo(w * 0.45f, h * 0.50f)
        lineTo(w * 0.45f, h * 0.57f)
        lineTo(w * 0.25f, h * 0.60f)
        close()
    }
    drawPath(strap1, leatherTan)
    drawPath(strap1, darkTan, style = Stroke(width = 2f))

    val strap2 = Path().apply {
        moveTo(w * 0.76f, h * 0.52f)
        lineTo(w * 0.55f, h * 0.50f)
        lineTo(w * 0.55f, h * 0.57f)
        lineTo(w * 0.75f, h * 0.60f)
        close()
    }
    drawPath(strap2, leatherTan)
    drawPath(strap2, darkTan, style = Stroke(width = 2f))

    // Traditional red pom-pom accent
    drawCircle(RoyalMaroon, radius = 8f, center = Offset(w * 0.50f, h * 0.35f))
    drawCircle(RoyalGold, radius = 3f, center = Offset(w * 0.50f, h * 0.35f))
}

private fun DrawScope.drawSliponArt(w: Float, h: Float) {
    val leather = Color(0xFF7B4A26)
    val highlight = Color(0xFFA0673B)

    val body = Path().apply {
        moveTo(w * 0.25f, h * 0.65f)
        cubicTo(w * 0.22f, h * 0.40f, w * 0.32f, h * 0.22f, w * 0.50f, h * 0.20f)
        cubicTo(w * 0.68f, h * 0.22f, w * 0.78f, h * 0.40f, w * 0.75f, h * 0.65f)
        cubicTo(w * 0.70f, h * 0.75f, w * 0.30f, h * 0.75f, w * 0.25f, h * 0.65f)
    }
    drawPath(body, Brush.verticalGradient(listOf(highlight, leather)))
    drawPath(body, Color(0xFF4A2B14), style = Stroke(width = 3f))

    // Hand-embossed medallion
    drawCircle(
        Color(0xFF5E3516),
        radius = 24f,
        center = Offset(w * 0.50f, h * 0.38f),
        style = Stroke(width = 2f)
    )
    drawCircle(
        RoyalGold,
        radius = 16f,
        center = Offset(w * 0.50f, h * 0.38f),
        style = Stroke(width = 1.5f)
    )
    drawCircle(
        Color(0xFF4A2B14),
        radius = 6f,
        center = Offset(w * 0.50f, h * 0.38f)
    )

    // Side elasticated insert
    drawRoundRect(
        color = Color(0xFF222222),
        topLeft = Offset(w * 0.32f, h * 0.50f),
        size = Size(w * 0.08f, 16f),
        cornerRadius = CornerRadius(4f)
    )
    drawRoundRect(
        color = Color(0xFF222222),
        topLeft = Offset(w * 0.60f, h * 0.50f),
        size = Size(w * 0.08f, 16f),
        cornerRadius = CornerRadius(4f)
    )
}

private fun DrawScope.drawSideFootwearIllustration(w: Float, h: Float, type: String, colorTheme: String) {
    val mainColor = when {
        colorTheme.contains("Maroon", ignoreCase = true) -> RoyalMaroon
        colorTheme.contains("Green", ignoreCase = true) -> Color(0xFF1B4D3E)
        colorTheme.contains("Black", ignoreCase = true) -> Color(0xFF222222)
        else -> RoyalMaroon
    }

    // Side profile curve
    val sideShoe = Path().apply {
        moveTo(w * 0.15f, h * 0.58f) // Heel base
        lineTo(w * 0.85f, h * 0.58f) // Ball to toe
        // Curved upturned toe
        cubicTo(w * 0.90f, h * 0.56f, w * 0.92f, h * 0.48f, w * 0.90f, h * 0.44f)
        cubicTo(w * 0.84f, h * 0.45f, w * 0.70f, h * 0.48f, w * 0.58f, h * 0.42f)
        // Collar dip
        cubicTo(w * 0.45f, h * 0.38f, w * 0.30f, h * 0.46f, w * 0.20f, h * 0.45f)
        // Heel back
        lineTo(w * 0.15f, h * 0.58f)
    }

    // Sole line
    drawLine(
        color = Color(0xFFC49A45),
        start = Offset(w * 0.14f, h * 0.60f),
        end = Offset(w * 0.86f, h * 0.60f),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    drawPath(sideShoe, mainColor)
    drawPath(sideShoe, RoyalGold, style = Stroke(width = 2.5f))

    // Decorative side embroidery line
    val embroidery = Path().apply {
        moveTo(w * 0.25f, h * 0.50f)
        cubicTo(w * 0.45f, h * 0.45f, w * 0.65f, h * 0.47f, w * 0.80f, h * 0.50f)
    }
    drawPath(embroidery, RoyalGold, style = Stroke(width = 2.2f))

    for (x in listOf(0.35f, 0.50f, 0.65f)) {
        drawCircle(Color.White, radius = 3.5f, center = Offset(w * x, h * 0.48f))
        drawCircle(RoyalGold, radius = 1.5f, center = Offset(w * x, h * 0.48f))
    }
}

private fun DrawScope.drawSoleIllustration(w: Float, h: Float) {
    // Vegetable tanned leather sole view showing hand-punching, grain & cushion
    val sole = Path().apply {
        moveTo(w * 0.30f, h * 0.70f)
        cubicTo(w * 0.26f, h * 0.50f, w * 0.34f, h * 0.28f, w * 0.50f, h * 0.20f)
        cubicTo(w * 0.66f, h * 0.28f, w * 0.74f, h * 0.50f, w * 0.70f, h * 0.70f)
        cubicTo(w * 0.65f, h * 0.78f, w * 0.35f, h * 0.78f, w * 0.30f, h * 0.70f)
    }
    drawPath(sole, Color(0xFFDFBA73))
    drawPath(sole, Color(0xFF986B1E), style = Stroke(width = 3f))

    // Bite-free cushion indicator circle
    drawCircle(
        color = Color(0x302E7D32),
        radius = 28f,
        center = Offset(w * 0.50f, h * 0.40f)
    )
    drawCircle(
        color = Color(0xFF2E7D32),
        radius = 28f,
        center = Offset(w * 0.50f, h * 0.40f),
        style = Stroke(width = 2f)
    )

    // Hand-stitched welt perimeter ticks
    for (i in 0..12) {
        val y = h * 0.26f + (i * 14f)
        drawLine(Color(0xFF6E480C), Offset(w * 0.32f, y), Offset(w * 0.36f, y), strokeWidth = 2f)
        drawLine(Color(0xFF6E480C), Offset(w * 0.64f, y), Offset(w * 0.68f, y), strokeWidth = 2f)
    }

    // Artisan Seal stamp in center
    drawCircle(RoyalMaroon.copy(alpha = 0.8f), radius = 16f, center = Offset(w * 0.50f, h * 0.62f), style = Stroke(width = 2f))
    drawCircle(RoyalMaroon.copy(alpha = 0.8f), radius = 8f, center = Offset(w * 0.50f, h * 0.62f))
}

private fun DrawScope.drawEmbroideryDetailIllustration(w: Float, h: Float, colorTheme: String) {
    val base = if (colorTheme.contains("Green", ignoreCase = true)) Color(0xFF163E32) else RoyalMaroon

    // Velvet background texture
    drawRect(base)

    // Close-up intricate Dabka and Zari vines
    val centerX = w * 0.5f
    val centerY = h * 0.5f

    drawCircle(RoyalGold, radius = 35f, center = Offset(centerX, centerY), style = Stroke(width = 3f))
    drawCircle(LightGold, radius = 22f, center = Offset(centerX, centerY), style = Stroke(width = 2f))
    drawCircle(Color.White, radius = 10f, center = Offset(centerX, centerY))

    // 8 Radiant petals
    for (i in 0 until 8) {
        val ang = (i * 45) * (Math.PI / 180.0)
        val px = centerX + (Math.cos(ang) * 48f).toFloat()
        val py = centerY + (Math.sin(ang) * 48f).toFloat()
        drawCircle(Color(0xFFFFF7E0), radius = 5.5f, center = Offset(px, py))
        drawCircle(RoyalGold, radius = 5.5f, center = Offset(px, py), style = Stroke(width = 1.5f))

        // Delicate gold wire leaf
        val lx = centerX + (Math.cos(ang) * 72f).toFloat()
        val ly = centerY + (Math.sin(ang) * 72f).toFloat()
        drawLine(RoyalGold, Offset(px, py), Offset(lx, ly), strokeWidth = 2.5f, cap = StrokeCap.Round)
        drawCircle(RoyalGold, radius = 3f, center = Offset(lx, ly))
    }
}

@Composable
fun RoyalCrest(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(40.dp)) {
        val w = size.width
        val h = size.height

        // Sunburst rays
        for (i in 0 until 12) {
            val ang = (i * 30) * (Math.PI / 180.0)
            val sx = (w / 2) + (Math.cos(ang) * (w * 0.28f)).toFloat()
            val sy = (h / 2) + (Math.sin(ang) * (h * 0.28f)).toFloat()
            val ex = (w / 2) + (Math.cos(ang) * (w * 0.44f)).toFloat()
            val ey = (h / 2) + (Math.sin(ang) * (h * 0.44f)).toFloat()
            drawLine(RoyalGold, Offset(sx, sy), Offset(ex, ey), strokeWidth = 2f)
        }

        // Royal medallion
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(RoyalGold, DeepGold),
                center = Offset(w / 2, h / 2),
                radius = w * 0.28f
            ),
            radius = w * 0.28f,
            center = Offset(w / 2, h / 2)
        )
        drawCircle(
            color = RoyalMaroon,
            radius = w * 0.20f,
            center = Offset(w / 2, h / 2)
        )
        drawCircle(
            color = RoyalGold,
            radius = w * 0.08f,
            center = Offset(w / 2, h / 2)
        )
    }
}
