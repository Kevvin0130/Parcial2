package com.example.parcial.domain.model
import java.math.BigDecimal
import java.time.LocalDate

data class Trip(
    val id: String,
    val name: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val notes: String = ""
)

data class Category(
    val id: String,
    val name: String
)

data class Expense(
    val id: String,
    val tripId: String,
    val description: String,
    val amount: BigDecimal,
    val category: Category,
    val date: LocalDate
)

data class TripDetail(
    val trip: Trip,
    val expenses: List<Expense>,
    val total: BigDecimal
)

/** Datos ya validados para crear un viaje. */
data class NewTrip(
    val name: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val notes: String
)

/** Datos ya validados para registrar un gasto. */
data class NewExpense(
    val tripId: String,
    val description: String,
    val amount: BigDecimal,
    val category: Category,
    val date: LocalDate
)

/** Datos crudos del formulario de viaje (pueden ser inválidos). */
data class TripForm(
    val name: String = "",
    val destination: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val notes: String = ""
)

/** Datos crudos del formulario de gasto (pueden ser inválidos). */
data class ExpenseForm(
    val description: String = "",
    val amount: String = "",
    val category: Category? = null,
    val date: LocalDate? = null
)
