package com.educalab.clasesmart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.clasesmart.domain.model.Expression
import com.educalab.clasesmart.ui.scene.CharacterSprite
import com.educalab.clasesmart.ui.theme.ClaseSmartColors as C
import com.educalab.clasesmart.ui.viewmodel.ProfileViewModel

@Composable
fun PerfilScreen(viewModel: ProfileViewModel, onExit: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(C.ParedCrema)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(50), color = C.MarcoMadera, onClick = onExit) {
                Text("← Aula", modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), color = C.TizaBlanca, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            Text("Tu perfil", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
        }
        Spacer(Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            CharacterSprite(Color(0xFFE8B98A), C.MarcoMaderaOscuro, C.AcentoAzulCielo, Expression.ORGULLOSO, sizeDp = 90)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(state.alias, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = C.TextoOscuro)
                Text("Grupo ${state.ageBand} años", color = C.TextoSuave)
            }
        }

        Spacer(Modifier.height(28.dp))
        SettingRow("Sonido", state.soundEnabled, viewModel::setSound)
        SettingRow("Vibracion (haptica)", state.hapticEnabled, viewModel::setHaptic)

        Spacer(Modifier.height(20.dp))
        Text(
            "ClaseSmart no guarda tu nombre real ni datos personales. Todo el progreso vive solo en este dispositivo.",
            color = C.TextoSuave, style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Surface(shape = RoundedCornerShape(14.dp), color = C.PapelBeige, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(Modifier.padding(14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = C.TextoOscuro)
            Switch(checked = checked, onCheckedChange = onChange, colors = SwitchDefaults.colors(checkedTrackColor = C.PizarraVerde))
        }
    }
}
