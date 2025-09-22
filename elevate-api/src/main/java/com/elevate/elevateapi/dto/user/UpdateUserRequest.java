package com.elevate.elevateapi.dto.user;

public record UpdateUserRequest (
        String username,
        String email,
        String gender,
        Integer age,
        String preferredUnitSystem,
        Double height,
        Double weight
){}
