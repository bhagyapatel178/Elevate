package com.elevate.elevateapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private double weightKg;
    private int reps;

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
