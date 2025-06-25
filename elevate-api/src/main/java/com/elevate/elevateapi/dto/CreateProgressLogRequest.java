package com.elevate.elevateapi.dto;

public record CreateProgressLogRequest (
        String liftType,
        String variation,
        Double weight,
        Integer reps
){}
