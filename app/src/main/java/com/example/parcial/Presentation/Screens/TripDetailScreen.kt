
package com.example.parcial.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial.domain.model.TripDetail
import com.example.parcial.domain.model.Expense

@Composable
fun TripDetailScreen(
    detail: TripDetail,
    onBack: () -> Unit,
    onAddExpense: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            TextButton(onClick = onBack) {
                Text("← Volver")
            }

            Text(
                text = detail.trip.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = detail.trip.destination,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Inicio: ${detail.trip.startDate}"
            )

            Text(
                text = "Finalización: ${detail.trip.endDate}"
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Total de gastos",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "$ ${detail.total.toPlainString()}",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = "${detail.expenses.size} gastos registrados"
                    )
                }
            }
        }

        item {
            Button(
                onClick = onAddExpense,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Registrar gasto")
            }
        }

        item {
            Text(
                text = "Historial de gastos",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (detail.expenses.isEmpty()) {
            item {
                Text("Todavía no hay gastos registrados.")
            }
        } else {
            items(
                items = detail.expenses,
                key = { it.id }
            ) { expense ->
                ExpenseItem(expense)
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.description,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = expense.category.name,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = expense.date.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "$ ${expense.amount.toPlainString()}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
