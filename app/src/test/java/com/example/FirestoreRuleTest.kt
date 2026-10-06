package com.example

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.firestore.FirestoreMemberRepository
import com.example.data.model.Member
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FirestoreRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun `authenticated user can save and read member`() = runBlocking {
    signInTestUser("admin_test@congregacao.org")
    val repository = FirestoreMemberRepository(firestore)

    val member = Member(
      id = 101L,
      fullName = "Lázaro Luis",
      congregation = "Novo Golfe 01",
      designation = "Ancião",
      primaryPhone = "923456789",
      hasPrimaryWhatsApp = true,
      isPresent = true,
      notes = "Administrador"
    )

    repository.saveMember(member)

    val doc = firestore.collection("members").document("101").get().await()
    assertTrue(doc.exists())
    assertEquals("Lázaro Luis", doc.getString("fullName"))
    assertEquals("Novo Golfe 01", doc.getString("congregation"))
    assertEquals(true, doc.getBoolean("isPresent"))
  }

  @Test
  fun `unauthenticated user write is rejected by security rules`() = runBlocking {
    auth.signOut()
    val repository = FirestoreMemberRepository(firestore)

    val member = Member(
      id = 102L,
      fullName = "Salú Gonsalves",
      congregation = "Golfe Quintalão 2",
      designation = "Ancião",
      primaryPhone = "934567890",
      hasPrimaryWhatsApp = true,
      isPresent = true
    )

    try {
      repository.saveMember(member)
      fail("Expected exception when unauthenticated")
    } catch (e: Exception) {
      assertNotNull(e)
    }
  }

  @Test
  fun `attendance update is saved correctly in Firestore`() = runBlocking {
    signInTestUser("attendance_admin@congregacao.org")
    val repository = FirestoreMemberRepository(firestore)

    val member = Member(
      id = 103L,
      fullName = "Manuel Troco",
      congregation = "Novo Golfe 05",
      designation = "Ancião",
      primaryPhone = "945678901",
      hasPrimaryWhatsApp = true,
      isPresent = false
    )
    repository.saveMember(member)

    repository.updateAttendance(103L, true)

    val doc = firestore.collection("members").document("103").get().await()
    assertEquals(true, doc.getBoolean("isPresent"))
  }
}
