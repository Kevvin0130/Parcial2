package com.example.parcial.domain.usecase

import com.example.parcial.domain.model.Category
import com.example.parcial.domain.model.Expense
import com.example.parcial.domain.model.ExpenseForm
import com.example.parcial.domain.model.NewExpense
import com.example.parcial.domain.model.SaveResult
import com.example.parcial.domain.repository.ExpenseRepository
import com.example.parcial.domain.repository.TripRepository
import com.example.parcial.domain.validation.ExpenseValidator
import java.math.BigDecimal
import kotlin.collections.sortedByDescending
import kotlin.coroutines.cancellation.CancellationException

/** Req. 4: gastos del viaje, del más reciente al más antiguo. */
class GetExpensesUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(tripId: String): List<Expense> =
        expenseRepository.getExpenses(tripId).sortedByDescending { it.date }
}

/** Req. 5 + 8: registrar gasto validando campos, valor positivo y fecha dentro del viaje. */
class RegisterExpenseUseCase(
    private val tripRepository: TripRepository,
    private val expenseRepository: ExpenseRepository,
    private val validator: ExpenseValidator
) {
    suspend operator fun invoke(tripId: String, form: ExpenseForm): SaveResult<Expense> {
        return try {
            val trip = tripRepository.getTrip(tripId)
            val errors = validator.validate(form, trip)
            if (errors.isNotEmpty()) return SaveResult.Invalid(errors)

            val newExpense = NewExpense(
                tripId = tripId,
                description = form.description.trim(),
                amount = requireNotNull(ExpenseValidator.parseAmount(form.amount)),
                category = requireNotNull(form.category),
                date = requireNotNull(form.date)
            )
            SaveResult.Success(expenseRepository.registerExpense(newExpense))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            SaveResult.Failure(e.message)
        }
    }
}

/** Req. 6: categorías disponibles. */
class GetCategoriesUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(): List<Category> = expenseRepository.getCategories()
}

/** Req. 7: total de gastos de un viaje. */
class CalculateTotalExpensesUseCase {
    operator fun invoke(expenses: List<Expense>): BigDecimal =
        expenses.fold(BigDecimal.ZERO) { acc, expense -> acc + expense.amount }
}
