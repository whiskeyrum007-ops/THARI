package com.example.data

import com.example.data.local.CartDao
import com.example.data.local.CartEntity
import com.example.data.local.OrderDao
import com.example.data.local.OrderEntity
import com.example.data.local.RewardDao
import com.example.data.local.RewardTransactionEntity
import com.example.data.local.WishlistDao
import com.example.data.local.WishlistEntity
import com.example.data.model.Product
import kotlinx.coroutines.flow.Flow

class ShopRepository(
    private val wishlistDao: WishlistDao,
    private val cartDao: CartDao,
    private val rewardDao: RewardDao,
    private val orderDao: OrderDao
) {
    val allProducts: List<Product> = ProductCatalog.products

    val wishlistItems: Flow<List<WishlistEntity>> = wishlistDao.getAllWishlist()
    val wishlistCount: Flow<Int> = wishlistDao.getWishlistCount()

    val cartItems: Flow<List<CartEntity>> = cartDao.getAllCart()
    val cartCount: Flow<Int> = cartDao.getCartCount()

    val rewardTransactions: Flow<List<RewardTransactionEntity>> = rewardDao.getAllTransactions()
    val rewardBalance: Flow<Int> = rewardDao.getTotalPoints()

    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()

    suspend fun initializeDefaultDataIfEmpty() {
        initializeDefaultRewardsIfEmpty()
        initializeDefaultOrdersIfEmpty()
    }

    suspend fun initializeDefaultRewardsIfEmpty() {
        if (rewardDao.getTransactionCount() == 0) {
            // Seed initial welcome bonus & initial heritage points
            rewardDao.insert(
                RewardTransactionEntity(
                    title = "Shahi Welcome Patron Gift",
                    points = 500,
                    type = "BONUS",
                    description = "Welcome bonus to the Dharti Rajasthan Royal Patron Club"
                )
            )
            rewardDao.insert(
                RewardTransactionEntity(
                    title = "Artisan Feedback Contribution",
                    points = 150,
                    type = "EARNED",
                    description = "Bonus for appreciating Barmer & Jodhpur karigar work"
                )
            )
        }
    }

    suspend fun initializeDefaultOrdersIfEmpty() {
        if (orderDao.getOrderCount() == 0) {
            orderDao.insert(
                OrderEntity(
                    orderId = "DR-92810",
                    itemsSummary = "Royal Jodhpuri Gold Zari Mojari (Size 8)",
                    itemCount = 1,
                    totalAmount = 2499,
                    status = "DELIVERED",
                    statusMessage = "Delivered to patron at Haveli No. 4, Johari Bazaar, Jaipur",
                    courierPartner = "Blue Dart Royal Air Express",
                    trackingAwb = "BLUEDART-RJ-928101",
                    origin = "Jodhpur Karigar Haveli, RJ",
                    destination = "Jaipur, RJ - 302001",
                    estimatedDelivery = "Delivered on Sep 22, 2026",
                    productType = "Mojari",
                    productColor = "Maroon",
                    timestamp = System.currentTimeMillis() - (86400000L * 2)
                )
            )
            orderDao.insert(
                OrderEntity(
                    orderId = "DR-48192",
                    itemsSummary = "Maharani Velvet Embroidered Jutti (Size 7)",
                    itemCount = 1,
                    totalAmount = 2199,
                    status = "DISPATCHED",
                    statusMessage = "In transit • Departed Jaipur Airport Sorting Hub",
                    courierPartner = "Blue Dart Royal Air Express",
                    trackingAwb = "BLUEDART-RJ-481920",
                    origin = "Jaipur Central Artisan Hub, RJ",
                    destination = "New Delhi, DL - 110001",
                    estimatedDelivery = "Arriving Tomorrow by 4:00 PM",
                    productType = "Jutti",
                    productColor = "Deep Red",
                    timestamp = System.currentTimeMillis() - 43200000L
                )
            )
            orderDao.insert(
                OrderEntity(
                    orderId = "DR-77341",
                    itemsSummary = "Barmer Hand-Tooled Kolhapuri (Size 9)",
                    itemCount = 1,
                    totalAmount = 1899,
                    status = "ARTISAN_CRAFTING",
                    statusMessage = "Master Karigar hand-stitching buff leather & cushioned sole in Barmer atelier",
                    courierPartner = "Delhivery Handcrafted Logistics",
                    trackingAwb = "DELHIVERY-RJ-773419",
                    origin = "Barmer Artisan Cluster, RJ",
                    destination = "Mumbai, MH - 400050",
                    estimatedDelivery = "Arriving in 3-4 Days",
                    productType = "Kolhapuri",
                    productColor = "Tan Brown",
                    timestamp = System.currentTimeMillis() - 14400000L
                )
            )
        }
    }

    fun getOrderById(orderId: String): Flow<OrderEntity?> = orderDao.getOrderById(orderId)

    suspend fun findOrder(orderId: String): OrderEntity? = orderDao.findOrder(orderId)

    suspend fun saveOrder(order: OrderEntity) {
        orderDao.insert(order)
    }

    suspend fun earnPoints(points: Int, orderId: String, orderAmount: Int) {
        if (points > 0) {
            rewardDao.insert(
                RewardTransactionEntity(
                    title = "Order #$orderId Reward",
                    points = points,
                    type = "EARNED",
                    description = "Earned 10% cashback coins on ₹$orderAmount footwear purchase",
                    orderId = orderId
                )
            )
        }
    }

    suspend fun redeemPoints(points: Int, orderId: String) {
        if (points > 0) {
            rewardDao.insert(
                RewardTransactionEntity(
                    title = "Redeemed on Order #$orderId",
                    points = -points,
                    type = "REDEEMED",
                    description = "Redeemed for ₹$points instant checkout concession",
                    orderId = orderId
                )
            )
        }
    }

    fun isWishlisted(productId: String): Flow<Boolean> = wishlistDao.isWishlisted(productId)

    suspend fun addToWishlist(product: Product) {
        val entity = WishlistEntity(
            productId = product.id,
            name = product.name,
            category = product.category,
            price = product.price,
            originalPrice = product.originalPrice,
            rating = product.rating,
            reviewCount = product.reviewCount,
            tag = product.tag,
            color = product.color
        )
        wishlistDao.insert(entity)
    }

    suspend fun removeFromWishlist(productId: String) {
        wishlistDao.deleteByProductId(productId)
    }

    suspend fun addToCart(product: Product, size: Int, quantity: Int = 1, discountType: String = "NONE") {
        val existing = cartDao.findCartItem(product.id, size)
        if (existing != null) {
            cartDao.updateQuantity(existing.id, existing.quantity + quantity)
        } else {
            cartDao.insert(
                CartEntity(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    originalPrice = product.originalPrice,
                    size = size,
                    quantity = quantity,
                    color = product.color,
                    discountType = discountType
                )
            )
        }
    }

    suspend fun updateCartQuantity(cartId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.deleteById(cartId)
        } else {
            cartDao.updateQuantity(cartId, newQuantity)
        }
    }

    suspend fun removeCartItem(cartId: Int) {
        cartDao.deleteById(cartId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
