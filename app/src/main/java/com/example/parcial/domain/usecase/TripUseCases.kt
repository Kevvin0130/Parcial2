package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.NewTrip
import com.example.parcial.domain.model.SaveResult
import com.example.parcial.domain.model.Trip
import com.example.parcial.domain.model.TripDetail
import com.example.parcial.domain.model.TripForm
import com.example.parcial.domain.repository.TripRepository
import com.example.parcial.domain.validation.TripValidator
import kotlin.coroutines.cancellation.CancellationException

/** Req. 1: consultar viajes. */
class GetTripsUseCase(private val tripRepository: TripRepository) {
    suspend operator fun invoke(): List<Trip> = tripRepository.getTrips()
}

/** Req. 2 + 8: crear viaje validando la información. */
class CreateTripUseCase(
    private val tripRepository: TripRepository,
    private val validator: TripValidator
) {
    suspend operator fun invoke(form: TripForm): SaveResult<Trip> {
        val errors = validator.validate(form)
        if (errors.isNotEmpty()) return SaveResult.Invalid(errors)

        val newTrip = NewTrip(
            name = form.name.trim(),
            destination = form.destination.trim(),
            startDate = requireNotNull(form.startDate),
            endDate = requireNotNull(form.endDate),
            notes = form.notes.trim()
        )
        return try {
            SaveResult.Success(tripRepository.createTrip(newTrip))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            SaveResult.Failure(e.message)
        }
    }
}

/** Req. 3: detalle del viaje con resumen de gastos. */
class GetTripDetailUseCase(
    private val tripRepository: TripRepository,
    private val getExpenses: GetExpensesUseCase,
    private val calculateTotal: CalculateTotalExpensesUseCase
) {
    suspend operator fun invoke(tripId: String): TripDetail {
        val trip = tripRepository.getTrip(tripId)
        val expenses = getExpenses(tripId)
        return TripDetail(trip = trip, expenses = expenses, total = calculateTotal(expenses))
    }
}

/** Obtiene un viaje (usado para validar/mostrar el período al registrar gastos). */
class GetTripUseCase(private val tripRepository: TripRepository) {
    suspend operator fun invoke(tripId: String) = tripRepository.getTrip(tripId)
}
