
package com.example.parcial

import com.example.parcial.domain.model.Trip
import com.example.parcial.domain.model.TripForm
import com.example.parcial.domain.model.ExpenseForm
import com.example.parcial.domain.model.Category
import com.example.parcial.domain.validation.TripValidator
import com.example.parcial.domain.validation.ExpenseValidator
import com.example.parcial.domain.validation.ValidationError
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.math.BigDecimal


class DomainValidationTest {

    private val tripValidator = TripValidator()
    private val expenseValidator = ExpenseValidator()

    @Test
    fun createTripWithValidDataReturnsNoErrors() {
        val form = TripForm(
            name = "Europa 2026",
            destination = "Italia",
            startDate = LocalDate.of(2026, 6, 10),
            endDate = LocalDate.of(2026, 6, 20)
        )

        val errors = tripValidator.validate(form)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun createTripWithoutNameReturnsError() {
        val form = TripForm(
            name = "",
            destination = "Italia",
            startDate = LocalDate.of(2026, 6, 10),
            endDate = LocalDate.of(2026, 6, 20)
        )

        val errors = tripValidator.validate(form)

        assertTrue(
            errors.contains(ValidationError.NAME_REQUIRED)
        )
    }

    @Test
    fun createTripWithEndDateBeforeStartDateReturnsError() {
        val form = TripForm(
            name = "Europa 2026",
            destination = "Italia",
            startDate = LocalDate.of(2026, 6, 20),
            endDate = LocalDate.of(2026, 6, 10)
        )

        val errors = tripValidator.validate(form)

        assertTrue(
            errors.contains(ValidationError.END_BEFORE_START)
        )
    }

    @Test
    fun registerExpenseWithValidDataReturnsNoErrors() {
        val trip = Trip(
            id = "1",
            name = "Europa 2026",
            destination = "Italia",
            startDate = LocalDate.of(2026, 6, 10),
            endDate = LocalDate.of(2026, 6, 20)
        )

        val category = Category(
            id = "1",
            name = "Alimentación"
        )

        val form = ExpenseForm(
            description = "Cena en Trastevere",
            amount = "28.50",
            category = category,
            date = LocalDate.of(2026, 6, 12)
        )

        val errors = expenseValidator.validate(form, trip)

        assertTrue(errors.isEmpty())
    }

    @Test
    fun registerExpenseWithNegativeAmountReturnsError() {
        val trip = Trip(
            id = "1",
            name = "Europa 2026",
            destination = "Italia",
            startDate = LocalDate.of(2026, 6, 10),
            endDate = LocalDate.of(2026, 6, 20)
        )

        val form = ExpenseForm(
            description = "Cena",
            amount = "-50",
            category = Category("1", "Alimentación"),
            date = LocalDate.of(2026, 6, 12)
        )

        val errors = expenseValidator.validate(form, trip)

        assertTrue(
            errors.contains(ValidationError.AMOUNT_NOT_POSITIVE)
        )
    }

    @Test
    fun registerExpenseOutsideTripDatesReturnsError() {
        val trip = Trip(
            id = "1",
            name = "Europa 2026",
            destination = "Italia",
            startDate = LocalDate.of(2026, 6, 10),
            endDate = LocalDate.of(2026, 6, 20)
        )

        val form = ExpenseForm(
            description = "Hotel",
            amount = "100",
            category = Category("2", "Alojamiento"),
            date = LocalDate.of(2026, 6, 25)
        )

        val errors = expenseValidator.validate(form, trip)

        assertTrue(
            errors.contains(
                ValidationError.DATE_OUT_OF_TRIP_RANGE
            )
        )
    }

    @Test
    fun parseAmountWithCommaReturnsCorrectBigDecimal() {
        val result = ExpenseValidator.parseAmount("28,50")

        assertEquals(
            BigDecimal("28.50"),
            result
        )
    }
}
