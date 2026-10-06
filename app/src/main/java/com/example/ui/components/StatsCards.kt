package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CongregationStats
import com.example.ui.theme.BaptizedPublisherColor
import com.example.ui.theme.DangerRed
import com.example.ui.theme.ElderBadgeColor
import com.example.ui.theme.MinisterialServantColor
import com.example.ui.theme.RegularPioneerColor
import com.example.ui.theme.WhatsAppGreenDark

@Composable
fun StatsRow(
    stats: CongregationStats,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatPill(
            label = "Total",
            count = stats.total,
            icon = Icons.Default.Group,
            color = MaterialTheme.colorScheme.primary,
            tag = "stat_total"
        )
        StatPill(
            label = "Presentes",
            count = stats.presentCount,
            icon = Icons.Default.CheckCircle,
            color = WhatsAppGreenDark,
            tag = "stat_present"
        )
        StatPill(
            label = "Ausentes",
            count = stats.absentCount,
            icon = Icons.Default.Cancel,
            color = DangerRed,
            tag = "stat_absent"
        )
        StatPill(
            label = "Anciãos",
            count = stats.elders,
            icon = Icons.Default.Security,
            color = ElderBadgeColor,
            tag = "stat_elders"
        )
        StatPill(
            label = "Servos Min.",
            count = stats.ministerialServants,
            icon = Icons.Default.AssignmentInd,
            color = MinisterialServantColor,
            tag = "stat_servants"
        )
        StatPill(
            label = "Pioneiros",
            count = stats.regularPioneers,
            icon = Icons.Default.Star,
            color = RegularPioneerColor,
            tag = "stat_pioneers"
        )
        StatPill(
            label = "Publicadores",
            count = stats.baptizedPublishers,
            icon = Icons.Default.CheckCircle,
            color = BaptizedPublisherColor,
            tag = "stat_publishers"
        )
    }
}

@Composable
private fun StatPill(
    label: String,
    count: Int,
    icon: ImageVector,
    color: Color,
    tag: String
) {
    Card(
        modifier = Modifier.testTag(tag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Column {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
