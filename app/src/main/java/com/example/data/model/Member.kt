package com.example.data.model

data class Member(
    val id: Long = 0,
    val fullName: String,
    val congregation: String,
    val designation: String,
    val primaryPhone: String,
    val hasPrimaryWhatsApp: Boolean,
    val secondaryPhone: String = "",
    val hasSecondaryWhatsApp: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isPresent: Boolean = false
)

data class PublicMember(
    val id: Long,
    val fullName: String,
    val congregation: String,
    val isPresent: Boolean = false
)

object CongregationConstants {
    val CONGREGATIONS = listOf(
        "Novo Golfe 01",
        "Novo Golfe 02",
        "Novo Golfe 03",
        "Novo Golfe 04",
        "Novo Golfe 05",
        "Novo Golfe 06",
        "Novo Golfe 07",
        "Novo Golfe 08",
        "Novo Golfe 09",
        "Novo Golfe 10",
        "Novo Golfe 11",
        "Novo Golfe 12",
        "Golfe Quintalão 2",
        "Golfe Quintalão 3",
        "Golfe Quintalão 4",
        "Golfe Quintalão 11"
    )

    val DESIGNATIONS = listOf(
        "Ancião",
        "Servo Ministerial",
        "Pioneiro Regular",
        "Publicador Batizado"
    )

    val ADMIN_USERS = listOf(
        "Lázaro Luis",
        "Salú Gonsalves",
        "Manuel Troco"
    )

    fun getDefaultPassword(adminName: String): String {
        return "$adminName 234"
    }

    fun validateAngolaPhone(phone: String): Boolean {
        val clean = phone.trim()
        return clean.length == 9 && clean.all { it.isDigit() }
    }

    fun getWhatsAppUrl(phone: String): String {
        val clean = phone.filter { it.isDigit() }
        return "https://wa.me/244$clean"
    }
}
