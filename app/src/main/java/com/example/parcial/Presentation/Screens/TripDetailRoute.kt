
package com.example.parcial.presentation.screens

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parcial.presentation.viewmodel.TripDetailViewModel
import com.example.parcial.presentation.viewmodel.TripDetailUiState

@Composable
fun TripDetailRoute(
    tripId: String,
    viewModel: TripDetailViewModel,
    onBack: () -> Unit,
    onAddExpense: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(tripId) {
        viewModel.loadTripDetail(tripId)
    }

    when (val currentState = state) {
        is TripDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is TripDetailUiState.Success -> {
            TripDetailScreen(
                detail = currentState.detail,
                onBack = onBack,
                onAddExpense = onAddExpense
            )
        }

        is TripDetailUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(currentState.message)
                Button(onClick = {
                    viewModel.loadTripDetail(tripId)
                }) {
                    Text("Reintentar")
                }
            }
        }
    }
}
