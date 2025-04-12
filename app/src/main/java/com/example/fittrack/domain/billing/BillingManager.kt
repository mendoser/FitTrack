package com.example.fittrack.domain.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(private val context: Context) : PurchasesUpdatedListener {

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    private val _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails: StateFlow<ProductDetails?> = _productDetails

    private val _billingConnectionState = MutableStateFlow(BillingConnectionState.DISCONNECTED)
    val billingConnectionState: StateFlow<BillingConnectionState> = _billingConnectionState

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    companion object {
        private const val PREMIUM_SUBSCRIPTION_ID = "premium_subscription"
    }

    init {
        connectToBillingService()
    }

    private fun connectToBillingService() {
        _billingConnectionState.value = BillingConnectionState.CONNECTING
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _billingConnectionState.value = BillingConnectionState.CONNECTED
                    CoroutineScope(Dispatchers.IO).launch {
                        queryProductDetails()
                        checkPremiumStatus()
                    }
                } else {
                    _billingConnectionState.value = BillingConnectionState.ERROR
                }
            }

            override fun onBillingServiceDisconnected() {
                _billingConnectionState.value = BillingConnectionState.DISCONNECTED
                connectToBillingService()
            }
        })
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (purchase.products.contains(PREMIUM_SUBSCRIPTION_ID)) {
                _isPremium.value = true
            }

            if (!purchase.isAcknowledged) {
                val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                CoroutineScope(Dispatchers.IO).launch {
                    billingClient.acknowledgePurchase(acknowledgeParams) { result ->
                        // Optionally handle result
                    }
                }
            }
        }
    }

    private suspend fun queryProductDetails() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PREMIUM_SUBSCRIPTION_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        val result = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params)
        }

        if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            val details = result.productDetailsList
            if (!details.isNullOrEmpty()) {
                _productDetails.value = details[0]
            }
        }
    }

    suspend fun checkPremiumStatus() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val purchasesResult = withContext(Dispatchers.IO) {
            billingClient.queryPurchasesAsync(params)
        }

        if (purchasesResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            val hasPremium = purchasesResult.purchasesList.any { purchase ->
                purchase.products.contains(PREMIUM_SUBSCRIPTION_ID) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            }
            _isPremium.value = hasPremium
        }
    }

    fun launchBillingFlow(activity: Activity) {
        val productDetails = _productDetails.value ?: return

        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: return

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken)
                        .build()
                )
            )
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }
}

enum class BillingConnectionState {
    CONNECTED,
    CONNECTING,
    DISCONNECTED,
    ERROR
}
