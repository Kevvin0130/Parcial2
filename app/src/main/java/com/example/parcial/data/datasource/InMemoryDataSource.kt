
package com.example.parcial.data.datasource

import com.example.parcial.domain.model.*
import java.util.UUID

class InMemoryDataSource {

    val trips = mutableListOf<Trip>()
    val expenses = mutableListOf<Expense>()

    val categories = listOf(
        Category("1", "Alimentación"),
        Category("2", "Transporte"),
        Category("3", "Alojamiento"),
        Category("4", "Entretenimiento"),
        Category("5", "Otros")
    )

    fun generateId(): String = UUID.randomUUID().toString()
}
