package com.example.data.repository

import com.example.data.local.MemberDao
import com.example.data.local.MemberEntity
import com.example.data.model.Member
import com.example.data.model.PublicMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemberRepository(private val dao: MemberDao) {

    val allMembers: Flow<List<Member>> = dao.getAllMembers().map { entities ->
        entities.map { it.toDomain() }
    }

    val publicMembers: Flow<List<PublicMember>> = dao.getAllMembers().map { entities ->
        entities.map { it.toPublicDomain() }
    }

    suspend fun insertMember(member: Member): Long {
        return dao.insertMember(MemberEntity.fromDomain(member))
    }

    suspend fun updateMember(member: Member) {
        dao.updateMember(MemberEntity.fromDomain(member))
    }

    suspend fun updateAttendance(id: Long, isPresent: Boolean) {
        dao.updateAttendance(id, isPresent)
    }

    suspend fun setAllAttendance(isPresent: Boolean) {
        dao.setAllAttendance(isPresent)
    }

    suspend fun deleteMember(member: Member) {
        dao.deleteMember(MemberEntity.fromDomain(member))
    }

    suspend fun deleteMemberById(id: Long) {
        dao.deleteMemberById(id)
    }

    suspend fun getMemberById(id: Long): Member? {
        return dao.getMemberById(id)?.toDomain()
    }

    suspend fun getCount(): Int {
        return dao.getCount()
    }

    suspend fun insertAll(members: List<Member>) {
        dao.insertAll(members.map { MemberEntity.fromDomain(it) })
    }
}
