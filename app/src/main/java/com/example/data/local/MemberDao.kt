package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY fullName ASC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE congregation = :congregation ORDER BY fullName ASC")
    fun getMembersByCongregation(congregation: String): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    suspend fun getMemberById(id: Long): MemberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<MemberEntity>)

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Query("UPDATE members SET isPresent = :isPresent WHERE id = :id")
    suspend fun updateAttendance(id: Long, isPresent: Boolean)

    @Query("UPDATE members SET isPresent = :isPresent")
    suspend fun setAllAttendance(isPresent: Boolean)

    @Delete
    suspend fun deleteMember(member: MemberEntity)

    @Query("DELETE FROM members WHERE id = :id")
    suspend fun deleteMemberById(id: Long)

    @Query("SELECT COUNT(*) FROM members")
    suspend fun getCount(): Int
}
