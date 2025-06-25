package com.elevate.elevateapi.dto;

public record UpdateProgressLogRequest (
        String liftType,
        String variation,
        Double weight,
        Integer reps
){}
