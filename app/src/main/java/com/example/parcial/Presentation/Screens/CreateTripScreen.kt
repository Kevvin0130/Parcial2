package com.example.parcial.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial.domain.model.TripForm
import com.example.parcial.presentation.viewmodel.TripsUiState
import java.time.LocalDate

@Composable
fun CreateTripScreen(
    state: TripsUiState,
    onSave: (TripForm) -> Unit,
    onBack: () -> Unit,
    onCreated: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(state.created) {
        if (state.created) onCreated()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Volver")
        }

        Text(
            text = "Crear viaje",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del viaje") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destino") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Fecha de inicio") },
            placeholder = { Text("2026-06-10") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("Fecha de finalización") },
            placeholder = { Text("2026-06-20") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notas (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        state.validationError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                val start = runCatching { LocalDate.parse(startDate) }.getOrNull()
                val end = runCatching { LocalDate.parse(endDate) }.getOrNull()
                onSave(
                    TripForm(
                        name = name,
                        destination = destination,
                        startDate = start,
                        endDate = end,
                        notes = notes
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.saving
        ) {
            if (state.saving) {
                CircularProgressIndicator()
            } else {
                Text("Guardar viaje")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
