
package com.example.parcial.data.repository

import com.example.parcial.data.datasource.InMemoryDataSource
import com.example.parcial.domain.model.*
import com.example.parcial.domain.repository.TripRepository

class TripRepositoryImpl(
    private val dataSource: InMemoryDataSource
) : TripRepository {

    override suspend fun getTrips(): List<Trip> =
        dataSource.trips.toList()

    override suspend fun getTrip(tripId: String): Trip =
        dataSource.trips.find { it.id == tripId }
            ?: throw NoSuchElementException("Viaje no encontrado")

    override suspend fun createTrip(newTrip: NewTrip): Trip {
        val trip = Trip(
            id = dataSource.generateId(),
            name = newTrip.name,
            destination = newTrip.destination,
            startDate = newTrip.startDate,
            endDate = newTrip.endDate,
            notes = newTrip.notes
        )

        dataSource.trips.add(trip)
        return trip
    }
}
