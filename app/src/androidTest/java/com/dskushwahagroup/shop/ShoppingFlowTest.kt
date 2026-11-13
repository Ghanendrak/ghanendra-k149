package com.dskushwahagroup.shop

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class ShoppingFlowTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun openGadgetsAndAddPhone(sendOrder: (String) -> Unit = {}) {
        composeRule.setContent { DSKApp(sendWhatsApp = sendOrder) }
        composeRule.onNodeWithText("Businesses").performClick()
        composeRule.onNodeWithText("DS SMART GADGETS").performClick()
        composeRule.onNodeWithText("Android Smartphone").assertExists()
        composeRule.onAllNodesWithText("Add to cart").onFirst().performClick()
        composeRule.onNodeWithText("Cart (1)").performClick()
    }

    @Test
    fun cartSupportsQuantityChangesAndRemoval() {
        openGadgetsAndAddPhone()

        composeRule.onNodeWithContentDescription("Increase").performClick()
        composeRule.onNodeWithText("₹25998").assertExists()

        composeRule.onNodeWithText("Remove").performClick()
        composeRule.onNodeWithText("Your cart is empty").assertExists()
    }

    @Test
    fun checkoutCollectsDeliveryDetailsAndCreatesWhatsAppOrder() {
        val sentOrder = AtomicReference<String?>(null)
        openGadgetsAndAddPhone { sentOrder.set(it) }

        composeRule.onNodeWithText("Proceed to Checkout").performClick()
        composeRule.onNodeWithText("Full name").performTextInput("Demo Customer")
        composeRule.onNodeWithText("Mobile number").performTextInput("9876543210")
        composeRule.onNodeWithText("Full address / city / PIN code")
            .performTextInput("Mauranipur, Uttar Pradesh - 284204")
        composeRule.onNodeWithText("Place Order via WhatsApp").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) { sentOrder.get() != null }
        val message = sentOrder.get()
        assertNotNull(message)
        assertTrue(message!!.contains("Customer: Demo Customer"))
        assertTrue(message.contains("Mobile: 9876543210"))
        assertTrue(message.contains("Address: Mauranipur, Uttar Pradesh - 284204"))
        assertTrue(message.contains("Android Smartphone x1 = ₹12999"))
        assertTrue(message.contains("Demo subtotal: ₹12999"))
    }
}
