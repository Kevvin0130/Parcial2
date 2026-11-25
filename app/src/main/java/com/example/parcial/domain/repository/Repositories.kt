package com.example.parcial.domain.repository

import com.example.parcial.domain.model.Category
import com.example.parcial.domain.model.Expense
import com.example.parcial.domain.model.NewExpense
import com.example.parcial.domain.model.NewTrip
import com.example.parcial.domain.model.Trip

interface TripRepository {
    suspend fun getTrips(): List<Trip>
    suspend fun getTrip(tripId: String): Trip
    suspend fun createTrip(newTrip: NewTrip): Trip
}

interface ExpenseRepository {
    suspend fun getExpenses(tripId: String): List<Expense>
    suspend fun registerExpense(newExpense: NewExpense): Expense
    suspend fun getCategories(): List<Category>
}
