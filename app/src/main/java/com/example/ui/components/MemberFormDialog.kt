package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CongregationConstants
import com.example.data.model.Member
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenDark

@Composable
fun MemberFormDialog(
    initialMember: Member?,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        fullName: String,
        congregation: String,
        designation: String,
        primaryPhone: String,
        hasPrimaryWhatsApp: Boolean,
        secondaryPhone: String,
        hasSecondaryWhatsApp: Boolean,
        notes: String,
        isPresent: Boolean
    ) -> Unit
) {
    var fullName by remember { mutableStateOf(initialMember?.fullName ?: "") }
    var congregation by remember {
        mutableStateOf(initialMember?.congregation ?: CongregationConstants.CONGREGATIONS.first())
    }
    var designation by remember {
        mutableStateOf(initialMember?.designation ?: CongregationConstants.DESIGNATIONS.first())
    }
    var primaryPhone by remember { mutableStateOf(initialMember?.primaryPhone ?: "") }
    var hasPrimaryWhatsApp by remember { mutableStateOf(initialMember?.hasPrimaryWhatsApp ?: true) }
    var secondaryPhone by remember { mutableStateOf(initialMember?.secondaryPhone ?: "") }
    var hasSecondaryWhatsApp by remember { mutableStateOf(initialMember?.hasSecondaryWhatsApp ?: false) }
    var notes by remember { mutableStateOf(initialMember?.notes ?: "") }
    var isPresent by remember { mutableStateOf(initialMember?.isPresent ?: false) }

    var congregationExpanded by remember { mutableStateOf(false) }
    var designationExpanded by remember { mutableStateOf(false) }

    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    // Validation checks
    val isNameValid = fullName.trim().isNotBlank()
    val isPrimaryPhoneValid = CongregationConstants.validateAngolaPhone(primaryPhone.trim())
    val isSecondaryPhoneValid = secondaryPhone.trim().isEmpty() || CongregationConstants.validateAngolaPhone(secondaryPhone.trim())

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp)
                .testTag("member_form_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (initialMember == null) "Novo Cadastro" else "Editar Cadastro",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Controle de Pessoal da Congregação",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_form_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar formulário",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Form Fields
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Nome Completo
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Nome Completo *") },
                        placeholder = { Text("Ex: Manuel António da Costa") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        isError = hasAttemptedSubmit && !isNameValid,
                        supportingText = {
                            if (hasAttemptedSubmit && !isNameValid) {
                                Text("O nome completo é obrigatório", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_fullname")
                    )

                    // 2. Congregação (Select com as opções exatas)
                    Column {
                        Text(
                            text = "Congregação *",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box {
                            OutlinedTextField(
                                value = congregation,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Selecionar congregação"
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { congregationExpanded = true }
                                    .testTag("select_congregation"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                enabled = false
                            )

                            // Click interceptor overlay
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { congregationExpanded = true }
                            )

                            DropdownMenu(
                                expanded = congregationExpanded,
                                onDismissRequest = { congregationExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                CongregationConstants.CONGREGATIONS.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = option,
                                                fontWeight = if (option == congregation) FontWeight.Bold else FontWeight.Normal,
                                                color = if (option == congregation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        onClick = {
                                            congregation = option
                                            congregationExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 3. Designação (Select com as 4 opções)
                    Column {
                        Text(
                            text = "Designação *",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box {
                            OutlinedTextField(
                                value = designation,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Selecionar designação"
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("select_designation"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                enabled = false
                            )

                            // Click interceptor overlay
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { designationExpanded = true }
                            )

                            DropdownMenu(
                                expanded = designationExpanded,
                                onDismissRequest = { designationExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                CongregationConstants.DESIGNATIONS.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = option,
                                                fontWeight = if (option == designation) FontWeight.Bold else FontWeight.Normal,
                                                color = if (option == designation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        onClick = {
                                            designation = option
                                            designationExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 4. Contacto Principal (9 dígitos)
                    Column {
                        OutlinedTextField(
                            value = primaryPhone,
                            onValueChange = { input ->
                                val digitsOnly = input.filter { it.isDigit() }
                                if (digitsOnly.length <= 9) {
                                    primaryPhone = digitsOnly
                                }
                            },
                            label = { Text("Contacto Principal (Angola - 9 dígitos) *") },
                            placeholder = { Text("Ex: 923456789") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                Text(
                                    text = "${primaryPhone.length}/9",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (primaryPhone.length == 9) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = hasAttemptedSubmit && !isPrimaryPhoneValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isPrimaryPhoneValid) {
                                    Text(
                                        "Deve conter exatamente 9 dígitos numéricos (ex: 923456789)",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text("Formato nacional de Angola (+244)")
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_primary_phone")
                        )

                        // Checkbox WhatsApp Principal
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { hasPrimaryWhatsApp = !hasPrimaryWhatsApp }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = hasPrimaryWhatsApp,
                                onCheckedChange = { hasPrimaryWhatsApp = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = WhatsAppGreenDark
                                ),
                                modifier = Modifier.testTag("checkbox_primary_whatsapp")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Este contacto tem WhatsApp",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    // 5. Contacto Alternativo (opcional, 9 dígitos)
                    Column {
                        OutlinedTextField(
                            value = secondaryPhone,
                            onValueChange = { input ->
                                val digitsOnly = input.filter { it.isDigit() }
                                if (digitsOnly.length <= 9) {
                                    secondaryPhone = digitsOnly
                                }
                            },
                            label = { Text("Contacto Alternativo (Opcional - 9 dígitos)") },
                            placeholder = { Text("Ex: 912345678") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingIcon = {
                                if (secondaryPhone.isNotEmpty()) {
                                    Text(
                                        text = "${secondaryPhone.length}/9",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (secondaryPhone.length == 9) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.padding(end = 12.dp)
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = hasAttemptedSubmit && !isSecondaryPhoneValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isSecondaryPhoneValid) {
                                    Text(
                                        "Se preenchido, deve conter exatamente 9 dígitos",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_secondary_phone")
                        )

                        // Checkbox WhatsApp Alternativo
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { hasSecondaryWhatsApp = !hasSecondaryWhatsApp }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = hasSecondaryWhatsApp,
                                onCheckedChange = { hasSecondaryWhatsApp = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = WhatsAppGreenDark
                                ),
                                modifier = Modifier.testTag("checkbox_secondary_whatsapp")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Contacto alternativo tem WhatsApp",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    // 6. Frequência / Presença
                    Column {
                        Text(
                            text = "Estado Inicial de Presença",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isPresent = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = if (isPresent) {
                                    ButtonDefaults.outlinedButtonColors(
                                        containerColor = WhatsAppGreenDark,
                                        contentColor = androidx.compose.ui.graphics.Color.White
                                    )
                                } else {
                                    ButtonDefaults.outlinedButtonColors()
                                }
                            ) {
                                Text("Presente")
                            }

                            OutlinedButton(
                                onClick = { isPresent = false },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = if (!isPresent) {
                                    ButtonDefaults.outlinedButtonColors(
                                        containerColor = com.example.ui.theme.DangerRed,
                                        contentColor = androidx.compose.ui.graphics.Color.White
                                    )
                                } else {
                                    ButtonDefaults.outlinedButtonColors()
                                }
                            ) {
                                Text("Ausente")
                            }
                        }
                    }

                    // 7. Observações (Opcional)
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observações / Responsabilidades") },
                        placeholder = { Text("Ex: Coordenador, Territórios, etc.") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_notes"),
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("cancel_form_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            hasAttemptedSubmit = true
                            if (isNameValid && isPrimaryPhoneValid && isSecondaryPhoneValid) {
                                onSave(
                                    initialMember?.id ?: 0L,
                                    fullName,
                                    congregation,
                                    designation,
                                    primaryPhone,
                                    hasPrimaryWhatsApp,
                                    secondaryPhone,
                                    hasSecondaryWhatsApp,
                                    notes,
                                    isPresent
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_member_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Salvar")
                    }
                }
            }
        }
    }
}
