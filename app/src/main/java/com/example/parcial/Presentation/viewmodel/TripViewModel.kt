package com.example.parcial.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.domain.model.SaveResult
import com.example.parcial.domain.model.Trip
import com.example.parcial.domain.model.TripForm
import com.example.parcial.domain.usecase.CreateTripUseCase
import com.example.parcial.domain.usecase.GetTripsUseCase
import com.example.parcial.domain.validation.ValidationError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

 data class TripsUiState(
    val loading: Boolean = false,
    val saving: Boolean = false,
    val trips: List<Trip> = emptyList(),
    val error: String? = null,
    val validationError: String? = null,
    val created: Boolean = false
)

class TripViewModel(
    private val getTrips: GetTripsUseCase,
    private val createTrip: CreateTripUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TripsUiState())
    val state: StateFlow<TripsUiState> = _state.asStateFlow()

    fun loadTrips() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                _state.value = _state.value.copy(
                    loading = false,
                    trips = getTrips()
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = "No se pudieron cargar los viajes"
                )
            }
        }
    }

    fun saveTrip(form: TripForm) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                saving = true,
                error = null,
                validationError = null,
                created = false
            )

            when (val result = createTrip(form)) {
                is SaveResult.Success -> {
                    _state.value = _state.value.copy(
                        saving = false,
                        trips = getTrips(),
                        created = true
                    )
                }
                is SaveResult.Invalid -> {
                    _state.value = _state.value.copy(
                        saving = false,
                        validationError = result.errors.joinToString("\n") {
                            when (it) {
                                ValidationError.NAME_REQUIRED -> "Ingresa el nombre del viaje"
                                ValidationError.DESTINATION_REQUIRED -> "Ingresa el destino"
                                ValidationError.START_DATE_REQUIRED -> "Selecciona la fecha de inicio"
                                ValidationError.END_DATE_REQUIRED -> "Selecciona la fecha de finalización"
                                ValidationError.END_BEFORE_START -> "La fecha final no puede ser anterior a la inicial"
                                else -> "Revisa los datos ingresados"
                            }
                        }
                    )
                }
                is SaveResult.Failure -> {
                    _state.value = _state.value.copy(
                        saving = false,
                        error = result.message ?: "No se pudo guardar el viaje"
                    )
                }
            }
        }
    }

    fun clearCreated() {
        _state.value = _state.value.copy(created = false, validationError = null)
    }
}
