
package com.example.parcial

import com.example.parcial.domain.model.Category
import com.example.parcial.domain.model.Expense
import com.example.parcial.domain.usecase.CalculateTotalExpensesUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class CalculateTotalExpensesTest {

    private val useCase = CalculateTotalExpensesUseCase()

    @Test
    fun calculateTotalReturnsCorrectSum() {
        val category = Category("1", "Alimentación")

        val expenses = listOf(
            Expense(
                id = "1",
                tripId = "trip1",
                description = "Desayuno",
                amount = BigDecimal("20.00"),
                category = category,
                date = LocalDate.of(2026, 6, 10)
            ),
            Expense(
                id = "2",
                tripId = "trip1",
                description = "Almuerzo",
                amount = BigDecimal("35.00"),
                category = category,
                date = LocalDate.of(2026, 6, 11)
            ),
            Expense(
                id = "3",
                tripId = "trip1",
                description = "Cena",
                amount = BigDecimal("45.00"),
                category = category,
                date = LocalDate.of(2026, 6, 12)
            )
        )

        val total = useCase(expenses)

        assertEquals(
            BigDecimal("100.00"),
            total
        )
    }

    @Test
    fun calculateTotalWithEmptyExpensesReturnsZero() {
        val total = useCase(emptyList())

        assertEquals(BigDecimal.ZERO, total)
    }
}
