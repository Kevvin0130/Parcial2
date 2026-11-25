package com.example.parcial.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial.domain.model.Trip
import com.example.parcial.presentation.viewmodel.TripsUiState

@Composable
fun TripsScreen(
    state: TripsUiState,
    onCreate: () -> Unit,
    onTripClick: (String) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Mis viajes",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Organiza los gastos de tus viajes",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onCreate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Crear viaje")
            }
            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.error)
                        Button(onClick = onRetry) {
                            Text("Reintentar")
                        }
                    }
                }
                state.trips.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Todavía no tienes viajes registrados")
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.trips, key = { it.id }) { trip ->
                            TripCard(trip = trip, onClick = {
                                onTripClick(trip.id)
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TripCard(
    trip: Trip,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = trip.destination,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${trip.startDate} - ${trip.endDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text("›", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
