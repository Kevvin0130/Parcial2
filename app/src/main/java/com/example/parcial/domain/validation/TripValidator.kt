package com.example.parcial.domain.validation

import com.example.parcial.domain.model.TripForm
class TripValidator {

    fun validate(form: TripForm): Set<ValidationError> {
        val errors = mutableSetOf<ValidationError>()
        if (form.name.isBlank()) errors += ValidationError.NAME_REQUIRED
        if (form.destination.isBlank()) errors += ValidationError.DESTINATION_REQUIRED
        if (form.startDate == null) errors += ValidationError.START_DATE_REQUIRED
        if (form.endDate == null) errors += ValidationError.END_DATE_REQUIRED
        val start = form.startDate
        val end = form.endDate
        if (start != null && end != null && end.isBefore(start)) {
            errors += ValidationError.END_BEFORE_START
        }
        return errors
    }
}
