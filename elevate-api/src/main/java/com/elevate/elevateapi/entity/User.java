package com.elevate.elevateapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


    @Column(nullable = true)
    private String provider;   // e.g. "google"

    @Column(nullable = true, unique = true)
    private String providerId; // Google’s "sub" claim (their stable user id)


    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private int age;

    @Enumerated(EnumType.STRING)
    private MeasurementUnitSystem preferredUnitSystem; // cm,kg or ft,inches,lb

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
        METRIC, //cm, kg
        IMPERIAL //inches, lb
    }

    /*
    - user field in ProgressLog has the fk
    - cascade: deleting a user, deletes the progress log for that user
    - orphan removal: removing a log removes it from list, removes it from db
    */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgressLog> progressLogList = new ArrayList<>();


    //getters and setters for height and weight

    public double getHeight(){
        if (isImperial()){
            return this.heightCm * 0.393701; // cm -> inches
        }
        return this.heightCm;
    }

    public double getWeight(){
        if (isImperial()){
            return this.weightKg * 2.20462; // kg -> lb
        }
        return this.weightKg;
    }

    public void setHeight(double height){
        if (isImperial()){
            this.heightCm = height / 0.393701; // inches -> cm
        }else{
            this.heightCm = height;
        }
    }

    public void setWeight(double weight){
        if (isImperial()){
            this.weightKg = weight / 2.20462; // lb -> kg
        }
        else{
            this.weightKg = weight;
        }
    }

    public boolean isImperial(){
        return this.preferredUnitSystem == MeasurementUnitSystem.IMPERIAL;
    }

}
