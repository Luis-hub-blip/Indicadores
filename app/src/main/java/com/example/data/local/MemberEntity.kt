package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Member
import com.example.data.model.PublicMember

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true)
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
) {
    fun toDomain(): Member = Member(
        id = id,
        fullName = fullName,
        congregation = congregation,
        designation = designation,
        primaryPhone = primaryPhone,
        hasPrimaryWhatsApp = hasPrimaryWhatsApp,
        secondaryPhone = secondaryPhone,
        hasSecondaryWhatsApp = hasSecondaryWhatsApp,
        createdAt = createdAt,
        notes = notes,
        isPresent = isPresent
    )

    fun toPublicDomain(): PublicMember = PublicMember(
        id = id,
        fullName = fullName,
        congregation = congregation,
        isPresent = isPresent
    )

    companion object {
        fun fromDomain(member: Member): MemberEntity = MemberEntity(
            id = member.id,
            fullName = member.fullName,
            congregation = member.congregation,
            designation = member.designation,
            primaryPhone = member.primaryPhone,
            hasPrimaryWhatsApp = member.hasPrimaryWhatsApp,
            secondaryPhone = member.secondaryPhone,
            hasSecondaryWhatsApp = member.hasSecondaryWhatsApp,
            createdAt = member.createdAt,
            notes = member.notes,
            isPresent = member.isPresent
        )
    }
}
