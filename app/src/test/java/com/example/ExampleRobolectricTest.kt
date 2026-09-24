package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.ShopRepository
import com.example.data.local.AppDatabase
import com.example.data.local.OrderEntity
import com.example.data.local.WishlistEntity
import com.example.data.model.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: ShopRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ShopRepository(
            database.wishlistDao(),
            database.cartDao(),
            database.rewardDao(),
            database.orderDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Dharti Rajasthan", appName)
    }

    @Test
    fun `test persistent wishlist operations`() = runBlocking {
        val testProduct = Product(
            id = "test-mojari",
            name = "Royal Test Mojari",
            subtitle = "Handcrafted Test Footwear",
            category = "Men's Mojaris",
            type = "Mojari",
            gender = "Men",
            price = 2199,
            originalPrice = 3199,
            rating = 4.9f,
            reviewCount = 120,
            tag = "Heritage",
            artisanLocation = "Jodhpur, RJ",
            soleMaterial = "Buff Leather",
            upperMaterial = "Silk Zari",
            color = "Maroon",
            availableSizes = listOf(7, 8, 9),
            description = "Test Description",
            features = listOf("Feature 1"),
            careInstructions = "Care instructions"
        )

        // Initially empty
        val initialCount = repository.wishlistCount.first()
        assertEquals(0, initialCount)

        // Add to wishlist
        repository.addToWishlist(testProduct)
        val afterAddCount = repository.wishlistCount.first()
        assertEquals(1, afterAddCount)

        val items = repository.wishlistItems.first()
        assertEquals("test-mojari", items[0].productId)
        assertEquals("Royal Test Mojari", items[0].name)

        // Remove from wishlist
        repository.removeFromWishlist("test-mojari")
        val afterRemoveCount = repository.wishlistCount.first()
        assertEquals(0, afterRemoveCount)
    }

    @Test
    fun `test royal rewards loyalty earning and redemption`() = runBlocking {
        // Initialize default welcome rewards
        repository.initializeDefaultRewardsIfEmpty()

        val initialBalance = repository.rewardBalance.first()
        assertEquals(650, initialBalance)

        // Earn points on purchase
        repository.earnPoints(250, "DR-98210", 2499)
        val afterEarnBalance = repository.rewardBalance.first()
        assertEquals(900, afterEarnBalance)

        // Redeem points for discount
        repository.redeemPoints(200, "DR-98211")
        val afterRedeemBalance = repository.rewardBalance.first()
        assertEquals(700, afterRedeemBalance)

        // Verify transaction history log
        val transactions = repository.rewardTransactions.first()
        assertTrue(transactions.isNotEmpty())
        assertEquals(4, transactions.size) // 2 default + 1 earn + 1 redeem
        assertEquals("Redeemed on Order #DR-98211", transactions[0].title)
        assertEquals(-200, transactions[0].points)
    }

    @Test
    fun `test order tracking repository and live status query`() = runBlocking {
        // Initialize default orders
        repository.initializeDefaultOrdersIfEmpty()

        val orders = repository.allOrders.first()
        assertTrue("Seeded default orders should not be empty", orders.isNotEmpty())
        assertEquals(3, orders.size)

        // Query specific order by ID
        val deliveredOrder = repository.findOrder("DR-92810")
        assertNotNull(deliveredOrder)
        assertEquals("DELIVERED", deliveredOrder?.status)
        assertEquals("Royal Jodhpuri Gold Zari Mojari (Size 8)", deliveredOrder?.itemsSummary)

        // Query dispatched order
        val dispatchedOrder = repository.findOrder("DR-48192")
        assertNotNull(dispatchedOrder)
        assertEquals("DISPATCHED", dispatchedOrder?.status)
        assertEquals("BLUEDART-RJ-481920", dispatchedOrder?.trackingAwb)

        // Save a newly placed order and track it
        val newOrder = OrderEntity(
            orderId = "DR-11223",
            itemsSummary = "Maharani Velvet Embroidered Jutti (Size 8)",
            itemCount = 1,
            totalAmount = 2199,
            status = "CONFIRMED",
            statusMessage = "Order confirmed! Sourcing raw buff leather in Jaipur",
            courierPartner = "Blue Dart Royal Air Express",
            trackingAwb = "BLUEDART-RJ-112233",
            origin = "Jaipur Central Artisan Hub, RJ",
            destination = "Johari Bazaar, Jaipur, RJ - 302001",
            estimatedDelivery = "Arriving in 2-3 Days",
            productType = "Jutti",
            productColor = "Deep Red"
        )
        repository.saveOrder(newOrder)

        val tracked = repository.findOrder("DR-11223")
        assertNotNull(tracked)
        assertEquals("CONFIRMED", tracked?.status)
        assertEquals("Jutti", tracked?.productType)
        assertEquals("Deep Red", tracked?.productColor)
    }
}
