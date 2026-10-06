package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [MemberEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "congregations_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.memberDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: MemberDao) {
                val initialMembers = listOf(
                    MemberEntity(
                        fullName = "Manuel António da Costa",
                        congregation = "Novo Golfe 01",
                        designation = "Ancião",
                        primaryPhone = "923451234",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "912345678",
                        hasSecondaryWhatsApp = false,
                        notes = "Coordenador do corpo de anciãos",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Sebastião Domingos Afonso",
                        congregation = "Novo Golfe 01",
                        designation = "Servo Ministerial",
                        primaryPhone = "934567890",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "Responsável pelas contas",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Esperança Maria Panzo",
                        congregation = "Novo Golfe 01",
                        designation = "Pioneiro Regular",
                        primaryPhone = "945678901",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "921112233",
                        hasSecondaryWhatsApp = true,
                        notes = "",
                        isPresent = false
                    ),
                    MemberEntity(
                        fullName = "Joaquim Bernardo Neto",
                        congregation = "Novo Golfe 02",
                        designation = "Ancião",
                        primaryPhone = "926789012",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "Superintendente de serviço",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Teresa Garcia Cristóvão",
                        congregation = "Novo Golfe 02",
                        designation = "Publicador Batizado",
                        primaryPhone = "937890123",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "",
                        isPresent = false
                    ),
                    MemberEntity(
                        fullName = "Domingos Paulo Kiala",
                        congregation = "Novo Golfe 03",
                        designation = "Servo Ministerial",
                        primaryPhone = "948901234",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "990123456",
                        hasSecondaryWhatsApp = false,
                        notes = "Cuida dos territórios",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Mateus Fernando Luvualo",
                        congregation = "Novo Golfe 04",
                        designation = "Ancião",
                        primaryPhone = "929012345",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "Secretário da congregação",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Ana Paula Baptista",
                        congregation = "Novo Golfe 05",
                        designation = "Pioneiro Regular",
                        primaryPhone = "931234567",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "",
                        isPresent = false
                    ),
                    MemberEntity(
                        fullName = "Francisco Pedro Miguel",
                        congregation = "Novo Golfe 06",
                        designation = "Publicador Batizado",
                        primaryPhone = "942345678",
                        hasPrimaryWhatsApp = false,
                        secondaryPhone = "923987654",
                        hasSecondaryWhatsApp = true,
                        notes = "",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Carlos Alberto de Sousa",
                        congregation = "Novo Golfe 07",
                        designation = "Ancião",
                        primaryPhone = "993456789",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "Superintendente da Reunião Vida e Ministério",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Helena Vunge Simão",
                        congregation = "Novo Golfe 08",
                        designation = "Publicador Batizado",
                        primaryPhone = "924567891",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "",
                        isPresent = false
                    ),
                    MemberEntity(
                        fullName = "Gabriel Nzuzi Muanda",
                        congregation = "Golfe Quintalão 2",
                        designation = "Ancião",
                        primaryPhone = "935678912",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "911223344",
                        hasSecondaryWhatsApp = false,
                        notes = "Coordenador",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Bartolomeu Lando Mayamba",
                        congregation = "Golfe Quintalão 2",
                        designation = "Servo Ministerial",
                        primaryPhone = "946789123",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "Responsável pelos indicadores",
                        isPresent = false
                    ),
                    MemberEntity(
                        fullName = "Madalena Cassule Damião",
                        congregation = "Golfe Quintalão 3",
                        designation = "Pioneiro Regular",
                        primaryPhone = "927891234",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Afonso Zua Quaresma",
                        congregation = "Golfe Quintalão 4",
                        designation = "Ancião",
                        primaryPhone = "938912345",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "",
                        hasSecondaryWhatsApp = false,
                        notes = "",
                        isPresent = true
                    ),
                    MemberEntity(
                        fullName = "Simão Luamba Candido",
                        congregation = "Golfe Quintalão 11",
                        designation = "Servo Ministerial",
                        primaryPhone = "949123456",
                        hasPrimaryWhatsApp = true,
                        secondaryPhone = "925678123",
                        hasSecondaryWhatsApp = true,
                        notes = "Som e vídeo",
                        isPresent = false
                    )
                )
                dao.insertAll(initialMembers)
            }
        }
    }
}
