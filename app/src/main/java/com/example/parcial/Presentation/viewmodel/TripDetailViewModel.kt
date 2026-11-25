
package com.example.parcial.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial.domain.model.TripDetail
import com.example.parcial.domain.usecase.GetTripDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TripDetailUiState {
    data object Loading : TripDetailUiState
    data class Success(val detail: TripDetail) : TripDetailUiState
    data class Error(val message: String) : TripDetailUiState
}

class TripDetailViewModel(
    private val getTripDetail: GetTripDetailUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TripDetailUiState>(TripDetailUiState.Loading)

    val uiState: StateFlow<TripDetailUiState> =
        _uiState.asStateFlow()

    fun loadTripDetail(tripId: String) {
        viewModelScope.launch {
            _uiState.value = TripDetailUiState.Loading

            try {
                val detail = getTripDetail(tripId)
                _uiState.value = TripDetailUiState.Success(detail)
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = TripDetailUiState.Error(
                    e.message ?: "No se pudo cargar el viaje"
                )
            }
        }
    }
}
