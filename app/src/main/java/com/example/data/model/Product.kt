package com.example.data.model

data class Product(
    val id: String,
    val name: String,
    val subtitle: String,
    val category: String, // Men's Mojaris, Women's Juttis, Bridal & Festive, Daily Comforts
    val type: String, // Mojari, Jutti, Kolhapuri, Slip-on
    val gender: String, // Men, Women, Unisex
    val price: Int,
    val originalPrice: Int,
    val rating: Float,
    val reviewCount: Int,
    val tag: String,
    val artisanLocation: String,
    val soleMaterial: String,
    val upperMaterial: String,
    val color: String,
    val availableSizes: List<Int> = listOf(6, 7, 8, 9, 10, 11),
    val description: String,
    val features: List<String>,
    val careInstructions: String,
    val badge: String = "Bestseller"
) {
    val discountPercent: Int
        get() = if (originalPrice > 0) (((originalPrice - price).toFloat() / originalPrice) * 100).toInt() else 0
}

data class CategoryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String
)

enum class SpecialDiscount(
    val code: String,
    val title: String,
    val percent: Int,
    val idRequired: Boolean,
    val idPrompt: String
) {
    NONE("", "No Special Discount", 0, false, ""),
    RAJASTHAN("RAJASTHAN15", "Aapno Rajasthan Discount", 15, false, "Applicable to residents & deliveries within Rajasthan"),
    MILITARY("VEER20", "Veer Samman (Armed Forces)", 20, true, "Enter Military / Paramilitary Service ID Number"),
    STUDENT("YUVA18", "Yuva Shakti (Student Discount)", 18, true, "Enter University / College Student Roll / ID Number")
}
