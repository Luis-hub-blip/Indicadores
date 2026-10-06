package com.example.data.firestore

import android.content.Context
import com.example.R
import com.example.data.model.CongregationConstants
import com.example.data.model.Member
import com.example.data.model.PublicMember
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreMemberRepository(
    private val db: FirebaseFirestore
) {
    companion object {
        fun create(context: Context): FirestoreMemberRepository {
            val databaseId = context.applicationContext.getString(R.string.firestore_database_id)
            val firestore = FirebaseFirestore.getInstance(databaseId)
            return FirestoreMemberRepository(firestore)
        }
    }

    private val auth = Firebase.auth
    private val membersCollection = db.collection("members")
    private val adminAccountsCollection = db.collection("admin_accounts")

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("O utilizador deve estar autenticado com o Google para aceder ao Firestore.")
    }

    // Real-time observation of all members
    fun observeMembers(): Flow<List<Member>> = callbackFlow {
        val path = membersCollection.path
        val registration = membersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, path)
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val membersList = snapshot.documents.mapNotNull { doc ->
                    try {
                        val idStr = doc.getString("id") ?: doc.id
                        val idLong = idStr.filter { it.isDigit() }.toLongOrNull() ?: doc.id.hashCode().toLong().let { if (it < 0) -it else it }
                        Member(
                            id = idLong,
                            fullName = doc.getString("fullName") ?: "",
                            congregation = doc.getString("congregation") ?: "",
                            designation = doc.getString("designation") ?: "",
                            primaryPhone = doc.getString("primaryPhone") ?: "",
                            hasPrimaryWhatsApp = doc.getBoolean("hasPrimaryWhatsApp") ?: false,
                            secondaryPhone = doc.getString("secondaryPhone") ?: "",
                            hasSecondaryWhatsApp = doc.getBoolean("hasSecondaryWhatsApp") ?: false,
                            notes = doc.getString("notes") ?: "",
                            isPresent = doc.getBoolean("isPresent") ?: false
                        )
                    } catch (e: Exception) {
                        null
                    }
                }.sortedBy { it.fullName }

                trySend(membersList)
            }
        }

        awaitClose {
            registration.remove()
        }
    }

    // Real-time observation of public members
    fun observePublicMembers(): Flow<List<PublicMember>> = observeMembers().map { members ->
        members.map {
            PublicMember(
                id = it.id,
                fullName = it.fullName,
                congregation = it.congregation,
                isPresent = it.isPresent
            )
        }
    }

    suspend fun saveMember(member: Member) {
        val uid = requireUserId()
        val docId = if (member.id != 0L) member.id.toString() else "member_${System.currentTimeMillis()}"
        val docRef = membersCollection.document(docId)
        val path = docRef.path

        val payload = hashMapOf<String, Any>(
            "id" to docId,
            "fullName" to member.fullName,
            "congregation" to member.congregation,
            "designation" to member.designation,
            "primaryPhone" to member.primaryPhone,
            "hasPrimaryWhatsApp" to member.hasPrimaryWhatsApp,
            "secondaryPhone" to member.secondaryPhone,
            "hasSecondaryWhatsApp" to member.hasSecondaryWhatsApp,
            "notes" to member.notes,
            "isPresent" to member.isPresent,
            "updatedBy" to uid,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        try {
            val exists = docRef.get().await().exists()
            if (!exists) {
                payload["createdAt"] = FieldValue.serverTimestamp()
                docRef.set(payload).await()
            } else {
                docRef.update(payload).await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, if (member.id != 0L) OperationType.UPDATE else OperationType.CREATE, path)
            throw e
        }
    }

    suspend fun updateAttendance(id: Long, isPresent: Boolean) {
        val uid = requireUserId()
        val docId = id.toString()
        val docRef = membersCollection.document(docId)
        val path = docRef.path

        try {
            docRef.update(
                mapOf(
                    "isPresent" to isPresent,
                    "updatedBy" to uid,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            throw e
        }
    }

    suspend fun setAllAttendance(isPresent: Boolean) {
        val uid = requireUserId()
        try {
            val snapshot = membersCollection.get().await()
            val batch = db.batch()
            for (doc in snapshot.documents) {
                batch.update(
                    doc.reference,
                    mapOf(
                        "isPresent" to isPresent,
                        "updatedBy" to uid,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )
            }
            batch.commit().await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, membersCollection.path)
            throw e
        }
    }

    suspend fun deleteMember(id: Long) {
        val docId = id.toString()
        val docRef = membersCollection.document(docId)
        val path = docRef.path

        try {
            docRef.delete().await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            throw e
        }
    }

    // Admin account operations
    suspend fun getAdminPassword(adminName: String): String? {
        val safeDocId = adminName.replace(" ", "_")
        val docRef = adminAccountsCollection.document(safeDocId)
        return try {
            val doc = docRef.get().await()
            if (doc.exists()) {
                doc.getString("password")
            } else {
                null
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            null
        }
    }

    suspend fun saveAdminPassword(adminName: String, password: String) {
        val safeDocId = adminName.replace(" ", "_")
        val docRef = adminAccountsCollection.document(safeDocId)
        val payload = mapOf(
            "adminName" to adminName,
            "password" to password,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    // Seed initial members into Firestore if empty
    suspend fun seedInitialDataIfEmpty(initialMembers: List<Member>) {
        try {
            val snapshot = membersCollection.limit(1).get().await()
            if (snapshot.isEmpty) {
                val batch = db.batch()
                initialMembers.forEach { member ->
                    val docId = if (member.id != 0L) member.id.toString() else "member_${System.currentTimeMillis()}_${(100..999).random()}"
                    val docRef = membersCollection.document(docId)
                    val data = hashMapOf<String, Any>(
                        "id" to docId,
                        "fullName" to member.fullName,
                        "congregation" to member.congregation,
                        "designation" to member.designation,
                        "primaryPhone" to member.primaryPhone,
                        "hasPrimaryWhatsApp" to member.hasPrimaryWhatsApp,
                        "secondaryPhone" to member.secondaryPhone,
                        "hasSecondaryWhatsApp" to member.hasSecondaryWhatsApp,
                        "notes" to member.notes,
                        "isPresent" to member.isPresent,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                    batch.set(docRef, data)
                }
                batch.commit().await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, membersCollection.path)
        }
    }
}
