package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ShopRepository
import com.example.data.local.AppDatabase
import com.example.data.model.SpecialDiscount
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileDialog
import com.example.ui.screens.TrackOrderDialog
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.DhartiRajasthanTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = ShopRepository(
            database.wishlistDao(),
            database.cartDao(),
            database.rewardDao(),
            database.orderDao()
        )

        setContent {
            DhartiRajasthanTheme {
                val viewModel: MainViewModel = viewModel {
                    MainViewModel(repository)
                }

                DhartiRajasthanApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DhartiRajasthanApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val wishlistItems by viewModel.wishlistItems.collectAsStateWithLifecycle()
    val wishlistCount by viewModel.wishlistCount.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartCount by viewModel.cartCount.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
    val activeDiscount by viewModel.activeDiscount.collectAsStateWithLifecycle()

    // Royal Rewards state
    val rewardBalance by viewModel.rewardBalance.collectAsStateWithLifecycle()
    val rewardTransactions by viewModel.rewardTransactions.collectAsStateWithLifecycle()
    val redeemedPoints by viewModel.redeemedPoints.collectAsStateWithLifecycle()

    // Order Tracking state
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val showTrackOrderDialog by viewModel.showTrackOrderDialog.collectAsStateWithLifecycle()
    val trackingOrderId by viewModel.trackingOrderId.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showProfileDialog by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }

    // Listen to ViewModel snackbar events
    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Intercept hardware back button when in child screens
    BackHandler(enabled = currentScreen != Screen.HOME) {
        viewModel.navigateTo(Screen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "screen_crossfade"
        ) { screen ->
            when (screen) {
                Screen.HOME -> {
                    HomeScreen(
                        products = filteredProducts,
                        wishlistItems = wishlistItems,
                        wishlistCount = wishlistCount,
                        cartCount = cartCount,
                        royalPoints = rewardBalance,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        filterState = filterState,
                        activeDiscount = activeDiscount,
                        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onCategorySelected = { viewModel.onCategorySelected(it) },
                        onFilterApplied = { viewModel.onFilterApplied(it) },
                        onSelectDiscount = { viewModel.onSelectDiscount(it) },
                        onProductClicked = { viewModel.openProductDetail(it) },
                        onWishlistToggle = { viewModel.toggleWishlist(it) },
                        onAddToCart = { prod, sz -> viewModel.addToCart(prod, sz) },
                        onNavigateWishlist = { viewModel.navigateTo(Screen.WISHLIST) },
                        onNavigateCart = { viewModel.navigateTo(Screen.CART) },
                        onOpenProfile = { showProfileDialog = true },
                        onTrackOrderClicked = { viewModel.openTrackOrder(null) }
                    )
                }

                Screen.PDP -> {
                    val product = selectedProduct
                    if (product != null) {
                        val isWishlisted = wishlistItems.any { it.productId == product.id }
                        ProductDetailScreen(
                            product = product,
                            isWishlisted = isWishlisted,
                            onBackClicked = { viewModel.navigateTo(Screen.HOME) },
                            onWishlistToggle = { viewModel.toggleWishlist(product) },
                            onAddToCart = { size, qty, disc ->
                                viewModel.addToCart(product, size, qty, disc)
                            },
                            onBuyNow = { size, disc ->
                                viewModel.addToCart(product, size, 1, disc)
                                viewModel.navigateTo(Screen.CART)
                            }
                        )
                    } else {
                        viewModel.navigateTo(Screen.HOME)
                    }
                }

                Screen.WISHLIST -> {
                    WishlistScreen(
                        wishlistItems = wishlistItems,
                        onBackClicked = { viewModel.navigateTo(Screen.HOME) },
                        onItemClicked = { prodId -> viewModel.openProductById(prodId) },
                        onRemoveFromWishlist = { prodId -> viewModel.removeFromWishlist(prodId) },
                        onMoveToCart = { entity -> viewModel.moveWishlistItemToCart(entity) },
                        onExploreClicked = { viewModel.navigateTo(Screen.HOME) }
                    )
                }

                Screen.CART -> {
                    CartScreen(
                        cartItems = cartItems,
                        rewardBalance = rewardBalance,
                        redeemedPoints = redeemedPoints,
                        onRedeemPoints = { viewModel.setRedeemedPoints(it) },
                        onBackClicked = { viewModel.navigateTo(Screen.HOME) },
                        onUpdateQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
                        onRemoveItem = { id -> viewModel.removeCartItem(id) },
                        onProceedToCheckout = { showCheckoutDialog = true },
                        onExploreClicked = { viewModel.navigateTo(Screen.HOME) },
                        activeDiscount = activeDiscount,
                        onSelectDiscount = { viewModel.onSelectDiscount(it) }
                    )
                }
            }
        }
    }

    if (showProfileDialog) {
        ProfileDialog(
            rewardBalance = rewardBalance,
            rewardTransactions = rewardTransactions,
            onDismiss = { showProfileDialog = false },
            onTrackOrderClicked = { orderId ->
                viewModel.openTrackOrder(orderId)
            }
        )
    }

    if (showCheckoutDialog) {
        val rawSubtotal = cartItems.sumOf { it.price * it.quantity }
        val discountPercent = activeDiscount.percent
        val discountSavings = (rawSubtotal * discountPercent) / 100
        val afterCouponSubtotal = rawSubtotal - discountSavings
        val actualRedeemedDiscount = min(redeemedPoints, min(rewardBalance, afterCouponSubtotal)).coerceAtLeast(0)
        val deliveryFee = if (rawSubtotal > 999 || rawSubtotal == 0) 0 else 99
        val finalTotal = (afterCouponSubtotal - actualRedeemedDiscount + deliveryFee).coerceAtLeast(0)

        CheckoutDialog(
            totalAmount = finalTotal,
            itemCount = cartItems.sumOf { it.quantity },
            pointsRedeemed = actualRedeemedDiscount,
            onOrderPlaced = { orderId, totalPaid, pointsUsed ->
                viewModel.processOrderPlaced(orderId, totalPaid, pointsUsed)
            },
            onTrackOrder = { orderId ->
                viewModel.openTrackOrder(orderId)
            },
            onDismiss = { showCheckoutDialog = false }
        )
    }

    if (showTrackOrderDialog) {
        TrackOrderDialog(
            initialOrderId = trackingOrderId,
            orders = allOrders,
            onDismiss = { viewModel.closeTrackOrder() }
        )
    }
}
