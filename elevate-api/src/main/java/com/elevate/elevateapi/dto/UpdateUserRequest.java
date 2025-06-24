package com.elevate.elevateapi.dto;

public record UpdateUserRequest (
        String username,
        String email,
        String gender,
        int age,
        String preferredUnitSystem,
        double height,
        double weight
){}
