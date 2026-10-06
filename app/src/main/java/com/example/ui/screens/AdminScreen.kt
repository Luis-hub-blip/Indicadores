package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CongregationConstants
import com.example.data.model.Member
import com.example.ui.CongregationViewModel
import com.example.ui.components.AdminMemberItem
import com.example.ui.components.ChangePasswordDialog
import com.example.ui.components.MemberFormDialog
import com.example.ui.components.StatsRow
import com.example.ui.theme.DangerRed
import com.example.ui.theme.WhatsAppGreenDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: CongregationViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val members by viewModel.adminFilteredMembers.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val currentAdmin by viewModel.currentAdmin.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCongregation by viewModel.selectedCongregationFilter.collectAsState()
    val selectedDesignation by viewModel.selectedDesignationFilter.collectAsState()
    val selectedAttendance by viewModel.selectedAttendanceFilter.collectAsState()

    var showFormDialog by remember { mutableStateOf(false) }
    var editingMember by remember { mutableStateOf<Member?>(null) }
    var memberToDelete by remember { mutableStateOf<Member?>(null) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Painel do Administrador",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Admin: ${currentAdmin ?: "Administrador"}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showChangePasswordDialog = true },
                        modifier = Modifier.testTag("change_admin_password_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Atualizar Palavra-passe",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.testTag("admin_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Terminar Sessão",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingMember = null
                    showFormDialog = true
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                text = {
                    Text(
                        text = "Novo Cadastro",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("add_member_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Stats Row
            StatsRow(stats = stats)

            // Quick Batch Attendance Actions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Controle de Frequência:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.markAllAttendance(true) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.testTag("mark_all_present_button")
                        ) {
                            Text(
                                text = "Todos Presentes",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = WhatsAppGreenDark
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.markAllAttendance(false) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.testTag("mark_all_absent_button")
                        ) {
                            Text(
                                text = "Todos Ausentes",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed
                            )
                        }
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearch(it) },
                placeholder = { Text("Pesquisar por nome, congregação ou telefone...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearch("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpar"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("admin_search_input")
            )

            // Attendance Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedAttendance == null,
                    onClick = { viewModel.setAttendanceFilter(null) },
                    label = { Text("Todos") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("admin_filter_attendance_all")
                )

                FilterChip(
                    selected = selectedAttendance == true,
                    onClick = { viewModel.setAttendanceFilter(if (selectedAttendance == true) null else true) },
                    label = { Text("Apenas Presentes (${stats.presentCount})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = WhatsAppGreenDark,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("admin_filter_attendance_present")
                )

                FilterChip(
                    selected = selectedAttendance == false,
                    onClick = { viewModel.setAttendanceFilter(if (selectedAttendance == false) null else false) },
                    label = { Text("Apenas Ausentes (${stats.absentCount})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DangerRed,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("admin_filter_attendance_absent")
                )
            }

            // Filter Row 1: Designation Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedDesignation == null,
                    onClick = { viewModel.setDesignationFilter(null) },
                    label = { Text("Todas Designações") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

                CongregationConstants.DESIGNATIONS.forEach { desig ->
                    FilterChip(
                        selected = selectedDesignation == desig,
                        onClick = {
                            viewModel.setDesignationFilter(if (selectedDesignation == desig) null else desig)
                        },
                        label = { Text(desig) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            // Filter Row 2: Congregation Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCongregation == null,
                    onClick = { viewModel.setCongregationFilter(null) },
                    label = { Text("Todas Congregações") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                    )
                )

                CongregationConstants.CONGREGATIONS.forEach { cong ->
                    FilterChip(
                        selected = selectedCongregation == cong,
                        onClick = {
                            viewModel.setCongregationFilter(if (selectedCongregation == cong) null else cong)
                        },
                        label = { Text(cong) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }

            // Results count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${members.size} registros encontrados",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // List of Members
            if (members.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Nenhum registro encontrado",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "Clique em '+ Novo Cadastro' para adicionar uma pessoa.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("admin_members_list"),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = members,
                        key = { it.id }
                    ) { member ->
                        AdminMemberItem(
                            member = member,
                            onEdit = {
                                editingMember = member
                                showFormDialog = true
                            },
                            onDelete = {
                                memberToDelete = member
                            },
                            onSetAttendance = { isPresent ->
                                viewModel.setAttendance(member.id, isPresent)
                            }
                        )
                    }
                }
            }
        }
    }

    // Member Form Dialog (Create / Edit)
    if (showFormDialog) {
        MemberFormDialog(
            initialMember = editingMember,
            onDismiss = {
                showFormDialog = false
                editingMember = null
            },
            onSave = { id, fullName, congregation, designation, primaryPhone, hasPrimaryWhatsApp, secondaryPhone, hasSecondaryWhatsApp, notes, isPresent ->
                val saved = viewModel.saveMember(
                    id = id,
                    fullName = fullName,
                    congregation = congregation,
                    designation = designation,
                    primaryPhone = primaryPhone,
                    hasPrimaryWhatsApp = hasPrimaryWhatsApp,
                    secondaryPhone = secondaryPhone,
                    hasSecondaryWhatsApp = hasSecondaryWhatsApp,
                    notes = notes,
                    isPresent = isPresent
                )
                if (saved) {
                    showFormDialog = false
                    editingMember = null
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (memberToDelete != null) {
        AlertDialog(
            onDismissRequest = { memberToDelete = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = "Confirmar Exclusão",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Deseja realmente eliminar o cadastro de \"${memberToDelete?.fullName}\"? Esta ação não pode ser desfeita.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        memberToDelete?.let { viewModel.deleteMember(it) }
                        memberToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.testTag("confirm_delete_button")
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { memberToDelete = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            currentAdminName = currentAdmin ?: "Administrador",
            onDismiss = { showChangePasswordDialog = false },
            onChangePassword = { oldPass, newPass ->
                val ok = viewModel.updateCurrentAdminPassword(oldPass, newPass)
                if (ok) {
                    showChangePasswordDialog = false
                }
                ok
            }
        )
    }
}
