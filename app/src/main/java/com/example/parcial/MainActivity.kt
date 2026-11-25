package com.example.parcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import com.example.parcial.presentation.screens.CreateTripScreen
import com.example.parcial.presentation.screens.TripsScreen
import com.example.parcial.data.datasource.InMemoryDataSource
import com.example.parcial.data.repository.ExpenseRepositoryImpl
import com.example.parcial.data.repository.TripRepositoryImpl
import com.example.parcial.domain.usecase.CalculateTotalExpensesUseCase
import com.example.parcial.domain.usecase.CreateTripUseCase
import com.example.parcial.domain.usecase.GetCategoriesUseCase
import com.example.parcial.domain.usecase.GetExpensesUseCase
import com.example.parcial.domain.usecase.GetTripDetailUseCase
import com.example.parcial.domain.usecase.GetTripsUseCase
import com.example.parcial.domain.validation.TripValidator
import com.example.parcial.presentation.screens.TripDetailRoute
import com.example.parcial.presentation.viewmodel.TripDetailViewModel
import com.example.parcial.presentation.viewmodel.TripViewModel
import com.example.parcial.ui.theme.ParcialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ParcialTheme {
                val dataSource = remember { InMemoryDataSource() }
                val tripRepository = remember { TripRepositoryImpl(dataSource) }
                val expenseRepository = remember { ExpenseRepositoryImpl(dataSource) }

                val getTrips = remember { GetTripsUseCase(tripRepository) }
                val createTrip = remember {
                    CreateTripUseCase(tripRepository, TripValidator())
                }
                val getExpenses = remember { GetExpensesUseCase(expenseRepository) }
                val calculateTotal = remember { CalculateTotalExpensesUseCase() }
                val getTripDetail = remember {
                    GetTripDetailUseCase(tripRepository, getExpenses, calculateTotal)
                }

                val tripsViewModel = remember {
                    TripViewModel(getTrips, createTrip)
                }
                val detailViewModel = remember {
                    TripDetailViewModel(getTripDetail)
                }

                val state by tripsViewModel.state.collectAsState()
                var screen by remember { mutableStateOf("trips") }
                var selectedTripId by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    tripsViewModel.loadTrips()
                }

                when (screen) {
                    "create" -> CreateTripScreen(
                        state = state,
                        onSave = tripsViewModel::saveTrip,
                        onBack = { screen = "trips" },
                        onCreated = {
                            tripsViewModel.clearCreated()
                            screen = "trips"
                        }
                    )
                    "detail" -> TripDetailRoute(
                        tripId = selectedTripId,
                        viewModel = detailViewModel,
                        onBack = { screen = "trips" },
                        onAddExpense = {}
                    )
                    else -> TripsScreen(
                        state = state,
                        onCreate = { screen = "create" },
                        onTripClick = { id ->
                            selectedTripId = id
                            screen = "detail"
                        },
                        onRetry = tripsViewModel::loadTrips
                    )
                }
            }
        }
    }
}
