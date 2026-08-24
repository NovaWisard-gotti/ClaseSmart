package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.data.local.entity.BadgeEntity
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

@Composable
fun InsigniasScreen(badges: List<BadgeEntity>, earnedIds: Set<String>, onExit: () -> Unit) {
    Column(Modifier.fillMaxSize().background(C.ParedCrema).padding(16.dp)) {
        Row {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Insignias (${earnedIds.size}/${badges.size})", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(badges) { badge ->
                val earned = badge.badgeId in earnedIds
                Surface(shape = RoundedCornerShape(16.dp), color = if (earned) C.TizaAmarilla else C.ParedCremaSombra) {
                    Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = if (earned) C.AcentoNaranja else C.TextoSuave.copy(alpha = 0.4f), modifier = Modifier.size(36.dp))
                        Spacer(Modifier.height(6.dp))
                        Text(badge.name, fontWeight = FontWeight.Bold, color = C.TextoOscuro, style = MaterialTheme.typography.bodyMedium)
                        Text(badge.description, color = C.TextoSuave, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
