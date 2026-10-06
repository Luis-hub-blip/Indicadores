package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CongregationConstants
import com.example.data.model.Member
import com.example.data.model.PublicMember
import com.example.ui.theme.BaptizedPublisherBg
import com.example.ui.theme.BaptizedPublisherColor
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerRedContainer
import com.example.ui.theme.ElderBadgeBg
import com.example.ui.theme.ElderBadgeColor
import com.example.ui.theme.MinisterialServantBg
import com.example.ui.theme.MinisterialServantColor
import com.example.ui.theme.RegularPioneerBg
import com.example.ui.theme.RegularPioneerColor
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.theme.WhatsAppGreenDark

@Composable
fun PublicMemberItem(
    member: PublicMember,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("public_member_${member.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Initials Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(member.fullName),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = member.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Congregação",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = member.congregation,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Attendance Status Badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (member.isPresent) WhatsAppGreen.copy(alpha = 0.15f) else DangerRedContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (member.isPresent) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (member.isPresent) WhatsAppGreenDark else DangerRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (member.isPresent) "Presente" else "Ausente",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (member.isPresent) WhatsAppGreenDark else DangerRed
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminMemberItem(
    member: Member,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetAttendance: (isPresent: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("admin_member_${member.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Avatar, Name, Edit & Delete buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getInitials(member.fullName),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = member.fullName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // Actions: Edit and Delete
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("edit_member_${member.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar ${member.fullName}",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("delete_member_${member.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar ${member.fullName}",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges Row: Congregation and Designation
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Congregation Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = member.congregation,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                // Designation Badge
                DesignationBadge(designation = member.designation)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Attendance clickable buttons (Presente / Ausente)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Frequência:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(start = 6.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Button Presente
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSetAttendance(true) }
                            .testTag("attendance_present_${member.id}"),
                        shape = RoundedCornerShape(8.dp),
                        color = if (member.isPresent) WhatsAppGreenDark else Color.Transparent,
                        border = if (member.isPresent) null else ButtonDefaults.outlinedButtonBorder
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = if (member.isPresent) Color.White else WhatsAppGreenDark,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Presente",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (member.isPresent) FontWeight.Bold else FontWeight.Medium,
                                    color = if (member.isPresent) Color.White else WhatsAppGreenDark
                                )
                            )
                        }
                    }

                    // Button Ausente
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSetAttendance(false) }
                            .testTag("attendance_absent_${member.id}"),
                        shape = RoundedCornerShape(8.dp),
                        color = if (!member.isPresent) DangerRed else Color.Transparent,
                        border = if (!member.isPresent) null else ButtonDefaults.outlinedButtonBorder
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if (!member.isPresent) Color.White else DangerRed,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Ausente",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (!member.isPresent) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!member.isPresent) Color.White else DangerRed
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 0.8.dp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Contact 1 (Principal)
            ContactRow(
                label = "Principal",
                phone = member.primaryPhone,
                hasWhatsApp = member.hasPrimaryWhatsApp,
                onWhatsAppClick = {
                    openWhatsApp(context, member.primaryPhone)
                },
                onCallClick = {
                    openDialer(context, member.primaryPhone)
                }
            )

            // Contact 2 (Alternativo - if filled)
            if (member.secondaryPhone.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                ContactRow(
                    label = "Alternativo",
                    phone = member.secondaryPhone,
                    hasWhatsApp = member.hasSecondaryWhatsApp,
                    onWhatsAppClick = {
                        openWhatsApp(context, member.secondaryPhone)
                    },
                    onCallClick = {
                        openDialer(context, member.secondaryPhone)
                    }
                )
            }

            // Notes if any
            if (member.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nota: ${member.notes}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun DesignationBadge(designation: String) {
    val (bgColor, textColor, icon) = when (designation) {
        "Ancião" -> Triple(ElderBadgeBg, ElderBadgeColor, Icons.Default.Security)
        "Servo Ministerial" -> Triple(MinisterialServantBg, MinisterialServantColor, Icons.Default.AssignmentInd)
        "Pioneiro Regular" -> Triple(RegularPioneerBg, RegularPioneerColor, Icons.Default.Star)
        else -> Triple(BaptizedPublisherBg, BaptizedPublisherColor, Icons.Default.CheckCircle)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = designation,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

@Composable
private fun ContactRow(
    label: String,
    phone: String,
    hasWhatsApp: Boolean,
    onWhatsAppClick: () -> Unit,
    onCallClick: () -> Unit
) {
    val formattedPhone = formatAngolaPhone(phone)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = formattedPhone,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dial button
            IconButton(
                onClick = onCallClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Ligar para $formattedPhone",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // WhatsApp Direct Action Button
            if (hasWhatsApp) {
                WhatsAppBadgeButton(onClick = onWhatsAppClick)
            }
        }
    }
}

@Composable
fun WhatsAppBadgeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("whatsapp_action_button"),
        color = WhatsAppGreen.copy(alpha = 0.15f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // WhatsApp Icon Circle
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(WhatsAppGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "W",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                )
            }
            Text(
                text = "WhatsApp",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = WhatsAppGreenDark,
                    fontSize = 11.sp
                )
            )
        }
    }
}

private fun formatAngolaPhone(phone: String): String {
    val clean = phone.filter { it.isDigit() }
    return if (clean.length == 9) {
        "${clean.substring(0, 3)} ${clean.substring(3, 6)} ${clean.substring(6, 9)}"
    } else {
        phone
    }
}

private fun getInitials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> ""
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().first()}${parts.last().first()}".uppercase()
    }
}

private fun openWhatsApp(context: Context, phone: String) {
    try {
        val url = CongregationConstants.getWhatsAppUrl(phone)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Não foi possível abrir o WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun openDialer(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:+244$phone")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Não foi possível abrir o discador", Toast.LENGTH_SHORT).show()
    }
}
