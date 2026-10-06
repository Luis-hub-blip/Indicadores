package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CongregationConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Congregações", appName)
    }

    @Test
    fun `verify angola phone number validation`() {
        // Valid 9 digits
        assertTrue(CongregationConstants.validateAngolaPhone("923456789"))
        assertTrue(CongregationConstants.validateAngolaPhone("934123456"))

        // Invalid lengths or characters
        assertFalse(CongregationConstants.validateAngolaPhone("92345678")) // 8 digits
        assertFalse(CongregationConstants.validateAngolaPhone("9234567890")) // 10 digits
        assertFalse(CongregationConstants.validateAngolaPhone("92345678a")) // non-digit
        assertFalse(CongregationConstants.validateAngolaPhone(""))
    }

    @Test
    fun `verify whatsapp url generation`() {
        val url = CongregationConstants.getWhatsAppUrl("923456789")
        assertEquals("https://wa.me/244923456789", url)
    }

    @Test
    fun `verify congregations list contains exact requested congregations`() {
        assertEquals(16, CongregationConstants.CONGREGATIONS.size)
        assertTrue(CongregationConstants.CONGREGATIONS.contains("Novo Golfe 01"))
        assertTrue(CongregationConstants.CONGREGATIONS.contains("Novo Golfe 12"))
        assertTrue(CongregationConstants.CONGREGATIONS.contains("Golfe Quintalão 2"))
        assertTrue(CongregationConstants.CONGREGATIONS.contains("Golfe Quintalão 11"))
    }

    @Test
    fun `verify admin users and default passwords`() {
        val admins = CongregationConstants.ADMIN_USERS
        assertEquals(3, admins.size)
        assertTrue(admins.contains("Lázaro Luis"))
        assertTrue(admins.contains("Salú Gonsalves"))
        assertTrue(admins.contains("Manuel Troco"))

        assertEquals("Lázaro Luis 234", CongregationConstants.getDefaultPassword("Lázaro Luis"))
        assertEquals("Salú Gonsalves 234", CongregationConstants.getDefaultPassword("Salú Gonsalves"))
        assertEquals("Manuel Troco 234", CongregationConstants.getDefaultPassword("Manuel Troco"))
    }
}
