package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.SchoolMaterial
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.MaterialesViewModel

/**
 * Modulo 3 - "Mision de materiales". El estante (izquierda) y la mesa de
 * trabajo (abajo) son dos zonas fisicas de la escena; tocar un material lo
 * mueve entre ellas (interaccion directa sobre objetos, no una lista con
 * checkboxes).
 */
@Composable
fun MisionMaterialesScreen(viewModel: MaterialesViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().background(C.ParedCrema).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            Text("Mision de materiales", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        }
        Spacer(Modifier.height(8.dp))
        Text(state.mission.activityTitle, color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        Text("Estante (toca para llevar a la mesa)", style = MaterialTheme.typography.titleMedium, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.shelfMaterials) { material ->
                MaterialCard(material, C.AcentoAzulCielo) { viewModel.moveToTable(material) }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Mesa de trabajo", style = MaterialTheme.typography.titleMedium, color = C.TextoOscuro, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Surface(shape = RoundedCornerShape(16.dp), color = C.SueloMadera.copy(alpha = 0.35f), modifier = Modifier.fillMaxWidth().heightIn(min = 90.dp)) {
            if (state.tableMaterials.isEmpty()) {
                Box(Modifier.padding(16.dp)) { Text("Arrastra o toca materiales del estante para traerlos aqui.", color = C.TextoSuave) }
            } else {
                Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.tableMaterials.forEach { material ->
                        MaterialCard(material, C.TizaAmarilla) { viewModel.returnToShelf(material) }
                    }
                }
            }
        }

        state.evaluation?.let {
            Spacer(Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (it.isReady) C.AcentoVerdeManzana else C.TizaRosa,
                modifier = Modifier.fillMaxWidth()
            ) { Text(state.consequenceText, modifier = Modifier.padding(12.dp), color = C.TextoOscuro) }
        }

        Spacer(Modifier.height(10.dp))
        Button(
            onClick = { viewModel.confirmMission() },
            colors = ButtonDefaults.buttonColors(containerColor = C.PizarraVerde),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Empezar la actividad", color = C.TizaBlanca) }
    }
}

@Composable
private fun MaterialCard(material: SchoolMaterial, color: androidx.compose.ui.graphics.Color, onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(12.dp), color = color, onClick = onClick) {
        Column(Modifier.padding(10.dp).width(90.dp)) {
            Text(material.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = C.TextoOscuro, maxLines = 2)
            if (material.isFragile) Text("fragil", style = MaterialTheme.typography.labelLarge, color = C.TextoOscuro.copy(alpha = 0.7f))
        }
    }
}
