package com.elevate.elevateapi.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "progress_log")
public class ProgressLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LiftType liftType;

    private LocalDate date;
    private String variation; // eg weighted for dips or inclined for db press
    private double weight;
    private int reps;

    public ProgressLog() {}

    public enum LiftType{
        BENCH_PRESS,
        SQUAT,
        DEADLIFT,
        DUMBBELL_PRESS,
        PULL_UPS,
        DIPS
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
