package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val category: String,
    val price: Int,
    val originalPrice: Int,
    val rating: Float,
    val reviewCount: Int,
    val tag: String,
    val color: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: String,
    val name: String,
    val price: Int,
    val originalPrice: Int,
    val size: Int,
    val quantity: Int = 1,
    val color: String,
    val discountType: String = "NONE"
)

@Entity(tableName = "reward_transactions")
data class RewardTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val points: Int, // positive for earned, negative for redeemed
    val type: String, // "EARNED", "REDEEMED", "BONUS"
    val description: String,
    val orderId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val itemsSummary: String,
    val itemCount: Int,
    val totalAmount: Int,
    val status: String, // "CONFIRMED", "ARTISAN_CRAFTING", "QUALITY_CHECK", "DISPATCHED", "OUT_FOR_DELIVERY", "DELIVERED"
    val statusMessage: String,
    val courierPartner: String = "Blue Dart Royal Air Express",
    val trackingAwb: String = "BLUEDART-RJ-982110",
    val origin: String = "Jaipur Central Artisan Hub, RJ",
    val destination: String = "Jaipur, Rajasthan - 302001",
    val estimatedDelivery: String = "In 2-3 Days",
    val productType: String = "Mojari",
    val productColor: String = "Maroon",
    val timestamp: Long = System.currentTimeMillis()
)
