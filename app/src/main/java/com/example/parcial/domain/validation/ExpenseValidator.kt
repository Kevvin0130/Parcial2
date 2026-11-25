package com.example.parcial.domain.validation

import com.example.parcial.domain.model.ExpenseForm
import com.example.parcial.domain.model.Trip
import java.math.BigDecimal

class ExpenseValidator {

    fun validate(form: ExpenseForm, trip: Trip): Set<ValidationError> {
        val errors = mutableSetOf<ValidationError>()
        if (form.description.isBlank()) errors += ValidationError.DESCRIPTION_REQUIRED

        val amountText = form.amount.trim()
        if (amountText.isEmpty()) {
            errors += ValidationError.AMOUNT_REQUIRED
        } else {
            val amount = parseAmount(amountText)
            when {
                amount == null -> errors += ValidationError.AMOUNT_INVALID
                amount <= BigDecimal.ZERO -> errors += ValidationError.AMOUNT_NOT_POSITIVE
            }
        }

        if (form.category == null) errors += ValidationError.CATEGORY_REQUIRED

        val date = form.date
        if (date == null) {
            errors += ValidationError.DATE_REQUIRED
        } else if (date.isBefore(trip.startDate) || date.isAfter(trip.endDate)) {
            errors += ValidationError.DATE_OUT_OF_TRIP_RANGE
        }
        return errors
    }

    companion object {
        /** Acepta "28.50" y "28,50". */
        fun parseAmount(text: String): BigDecimal? =
            text.trim().replace(',', '.').toBigDecimalOrNull()
    }
}
