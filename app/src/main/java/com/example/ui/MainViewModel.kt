package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ProductCatalog
import com.example.data.ShopRepository
import com.example.data.local.CartEntity
import com.example.data.local.RewardTransactionEntity
import com.example.data.local.WishlistEntity
import com.example.data.model.Product
import com.example.data.model.SpecialDiscount
import com.example.ui.components.FilterState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    PDP,
    WISHLIST,
    CART
}

class MainViewModel(
    private val repository: ShopRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.initializeDefaultDataIfEmpty()
        }
    }

    // Persistent Room data streams
    val wishlistItems: StateFlow<List<WishlistEntity>> = repository.wishlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistCount: StateFlow<Int> = repository.wishlistCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartItems: StateFlow<List<CartEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartCount: StateFlow<Int> = repository.cartCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Orders stream for Tracking
    val allOrders: StateFlow<List<com.example.data.local.OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Track Order Dialog state
    private val _showTrackOrderDialog = MutableStateFlow(false)
    val showTrackOrderDialog: StateFlow<Boolean> = _showTrackOrderDialog.asStateFlow()

    private val _trackingOrderId = MutableStateFlow<String?>("DR-92810")
    val trackingOrderId: StateFlow<String?> = _trackingOrderId.asStateFlow()

    fun openTrackOrder(orderId: String? = null) {
        if (!orderId.isNullOrBlank()) {
            _trackingOrderId.value = orderId
        }
        _showTrackOrderDialog.value = true
    }

    fun closeTrackOrder() {
        _showTrackOrderDialog.value = false
    }

    // Royal Rewards loyalty streams
    val rewardBalance: StateFlow<Int> = repository.rewardBalance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 650)

    val rewardTransactions: StateFlow<List<RewardTransactionEntity>> = repository.rewardTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _redeemedPoints = MutableStateFlow(0)
    val redeemedPoints: StateFlow<Int> = _redeemedPoints.asStateFlow()

    // Navigation & View state
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Filters & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    // Active discount
    private val _activeDiscount = MutableStateFlow(SpecialDiscount.NONE)
    val activeDiscount: StateFlow<SpecialDiscount> = _activeDiscount.asStateFlow()

    // Snackbar notifications
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    // Filtered & Sorted products
    val filteredProducts: StateFlow<List<Product>> = combine(
        _searchQuery,
        _selectedCategory,
        _filterState
    ) { query, category, filter ->
        var list = ProductCatalog.products

        // Category filter
        if (category != "all") {
            list = list.filter { prod ->
                when (category) {
                    "men" -> prod.gender == "Men"
                    "women" -> prod.gender == "Women"
                    "bridal" -> prod.category.contains("Bridal", ignoreCase = true)
                    "comfort" -> prod.category.contains("Comfort", ignoreCase = true)
                    else -> prod.category.contains(category, ignoreCase = true)
                }
            }
        }

        // Search query
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter { prod ->
                prod.name.lowercase().contains(q) ||
                        prod.subtitle.lowercase().contains(q) ||
                        prod.category.lowercase().contains(q) ||
                        prod.type.lowercase().contains(q) ||
                        prod.artisanLocation.lowercase().contains(q)
            }
        }

        // Footwear Type filter
        if (filter.footwearType != "All") {
            list = list.filter { it.type.equals(filter.footwearType, ignoreCase = true) }
        }

        // Gender filter
        if (filter.gender != "All") {
            list = list.filter { it.gender.equals(filter.gender, ignoreCase = true) }
        }

        // Size filter
        if (filter.size != null) {
            list = list.filter { it.availableSizes.contains(filter.size) }
        }

        // Sorting
        when (filter.sortBy) {
            "Price: Low to High" -> list.sortedBy { it.price }
            "Price: High to Low" -> list.sortedByDescending { it.price }
            "Top Rated" -> list.sortedByDescending { it.rating }
            else -> list
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductCatalog.products)

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(categoryId: String) {
        _selectedCategory.value = categoryId
    }

    fun onFilterApplied(filter: FilterState) {
        _filterState.value = filter
    }

    fun onSelectDiscount(discount: SpecialDiscount) {
        _activeDiscount.value = discount
        viewModelScope.launch {
            if (discount != SpecialDiscount.NONE) {
                _snackbarEvent.emit("${discount.title} Applied! Code: ${discount.code}")
            }
        }
    }

    fun setRedeemedPoints(points: Int) {
        val maxRedeemable = rewardBalance.value
        val safePoints = points.coerceIn(0, maxRedeemable)
        _redeemedPoints.value = safePoints
        viewModelScope.launch {
            if (safePoints > 0) {
                _snackbarEvent.emit("Applied ₹$safePoints Royal Rewards Discount! 👑")
            } else {
                _snackbarEvent.emit("Royal Rewards discount removed")
            }
        }
    }

    fun openProductDetail(product: Product) {
        _selectedProduct.value = product
        _currentScreen.value = Screen.PDP
    }

    fun openProductById(productId: String) {
        val prod = ProductCatalog.products.firstOrNull { it.id == productId }
        if (prod != null) {
            _selectedProduct.value = prod
            _currentScreen.value = Screen.PDP
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val isPresent = wishlistItems.value.any { it.productId == product.id }
            if (isPresent) {
                repository.removeFromWishlist(product.id)
                _snackbarEvent.emit("Removed from Wishlist")
            } else {
                repository.addToWishlist(product)
                _snackbarEvent.emit("Saved to Wishlist ❤️")
            }
        }
    }

    fun removeFromWishlist(productId: String) {
        viewModelScope.launch {
            repository.removeFromWishlist(productId)
            _snackbarEvent.emit("Removed from Wishlist")
        }
    }

    fun addToCart(product: Product, size: Int, quantity: Int = 1, discount: SpecialDiscount = SpecialDiscount.NONE) {
        viewModelScope.launch {
            repository.addToCart(product, size, quantity, discount.name)
            _snackbarEvent.emit("Added to Cart (Size $size) 🛍️")
        }
    }

    fun moveWishlistItemToCart(item: WishlistEntity) {
        val prod = ProductCatalog.products.firstOrNull { it.id == item.productId }
        if (prod != null) {
            viewModelScope.launch {
                val defaultSize = prod.availableSizes.firstOrNull() ?: 7
                repository.addToCart(prod, defaultSize, 1)
                repository.removeFromWishlist(item.productId)
                _snackbarEvent.emit("Moved to Cart! (Size $defaultSize)")
            }
        }
    }

    fun updateCartQuantity(cartId: Int, newQty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartId, newQty)
        }
    }

    fun removeCartItem(cartId: Int) {
        viewModelScope.launch {
            repository.removeCartItem(cartId)
            _snackbarEvent.emit("Item removed from Cart")
        }
    }

    fun processOrderPlaced(orderId: String, totalPaid: Int, pointsRedeemed: Int) {
        viewModelScope.launch {
            // Build items summary from current cart
            val currentItems = cartItems.value
            val firstItem = currentItems.firstOrNull()
            val itemsSummary = if (currentItems.isEmpty()) {
                "Handcrafted Royal Rajasthani Mojari"
            } else if (currentItems.size == 1) {
                "${firstItem?.name} (Size ${firstItem?.size})"
            } else {
                "${firstItem?.name} & ${currentItems.size - 1} other pair(s)"
            }

            val prodType = if (itemsSummary.contains("Jutti", ignoreCase = true)) "Jutti"
            else if (itemsSummary.contains("Kolhapuri", ignoreCase = true)) "Kolhapuri"
            else "Mojari"

            val randomAwb = "BLUEDART-RJ-${(100000..999999).random()}"

            // Save order into persistent database for live tracking
            repository.saveOrder(
                com.example.data.local.OrderEntity(
                    orderId = orderId,
                    itemsSummary = itemsSummary,
                    itemCount = currentItems.sumOf { it.quantity }.coerceAtLeast(1),
                    totalAmount = totalPaid,
                    status = "CONFIRMED",
                    statusMessage = "Order confirmed! Sourcing raw buff leather & assigning master karigar in Jodhpur workshop.",
                    courierPartner = "Blue Dart Royal Air Express",
                    trackingAwb = randomAwb,
                    origin = "Jodhpur Royal Atelier, RJ",
                    destination = "Jaipur, Rajasthan - 302001",
                    estimatedDelivery = "Arriving in 3-4 Days",
                    productType = prodType,
                    productColor = firstItem?.color ?: "Maroon",
                    timestamp = System.currentTimeMillis()
                )
            )

            // 1. Redeem points if used
            if (pointsRedeemed > 0) {
                repository.redeemPoints(pointsRedeemed, orderId)
            }

            // 2. Earn points: 10% of total paid amount (minimum 25 points)
            val earnedPoints = ((totalPaid * 10) / 100).coerceAtLeast(25)
            repository.earnPoints(earnedPoints, orderId, totalPaid)

            // 3. Clear cart and reset redeemed points
            repository.clearCart()
            _redeemedPoints.value = 0

            _snackbarEvent.emit("Order Confirmed! You earned +$earnedPoints Royal Points 👑")
        }
    }

    fun clearCartOnCheckout() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}
