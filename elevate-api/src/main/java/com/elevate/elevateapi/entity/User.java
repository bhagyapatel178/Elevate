package com.elevate.elevateapi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; // display name

    @Column(unique = true)
    private String email;

    private String password; // hash

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private int age;

    @Enumerated(EnumType.STRING)
    private MeasurementUnitSystem  preferredUnitSystem; // cm,kg or ft,inches,lb

    private double heightCm; // store in cm
    private double weightKg; // store in kg

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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

    public User() {}

}
