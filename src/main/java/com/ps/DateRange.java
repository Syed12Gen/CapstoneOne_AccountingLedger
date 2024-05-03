package com.ps;

import java.time.LocalDate;
import java.time.Period;

public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Period toPeriod() {
        return Period.between(startDate, endDate);
    }

    public boolean includes(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
