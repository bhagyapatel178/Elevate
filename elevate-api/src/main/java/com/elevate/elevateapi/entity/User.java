package com.elevate.elevateapi.entity;

import jakarta.persistence.*;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // display name
    private String email;
    private String password; // hash

    @Enumerated(EnumType.STRING)
    private GenderType gender;
    private int age;
    @Enumerated(EnumType.STRING)
    private MeasurementUnitSystem  preferredUnitSystem; // cm,kg or ft,inches,lb
    private double heightCm; // store in cm
    private double weightKg; // store in kg

    public enum GenderType {
        MALE,
        FEMALE,
        NON_BINARY,
        PREFER_NOT_TO_SAY
    }

    public enum MeasurementUnitSystem{
        METRIC,
        IMPERIAL
    }

}
