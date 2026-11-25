
package com.example.parcial.data.repository

import com.example.parcial.data.datasource.InMemoryDataSource
import com.example.parcial.domain.model.*
import com.example.parcial.domain.repository.ExpenseRepository

class ExpenseRepositoryImpl(
    private val dataSource: InMemoryDataSource
) : ExpenseRepository {

    override suspend fun getExpenses(tripId: String): List<Expense> =
        dataSource.expenses.filter { it.tripId == tripId }

    override suspend fun registerExpense(newExpense: NewExpense): Expense {
        val expense = Expense(
            id = dataSource.generateId(),
            tripId = newExpense.tripId,
            description = newExpense.description,
            amount = newExpense.amount,
            category = newExpense.category,
            date = newExpense.date
        )

        dataSource.expenses.add(expense)
        return expense
    }

    override suspend fun getCategories(): List<Category> =
        dataSource.categories
}
