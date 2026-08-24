package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.data.local.entity.ProjectEntity
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C

@Composable
fun ProyectosListScreen(projects: List<ProjectEntity>, onSelect: (String) -> Unit, onExit: () -> Unit) {
    Column(Modifier.fillMaxSize().background(C.ParedCrema).padding(16.dp)) {
        Row {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Gran Proyecto del Aula", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(projects) { project ->
                Surface(shape = RoundedCornerShape(16.dp), color = C.PapelBeige, onClick = { onSelect(project.projectId) }) {
                    Column(Modifier.padding(14.dp)) {
                        Text(project.title, fontWeight = FontWeight.Bold, color = C.TextoOscuro, style = MaterialTheme.typography.titleMedium)
                        Text(project.description, color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
