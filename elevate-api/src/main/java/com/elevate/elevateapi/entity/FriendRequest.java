package com.elevate.elevateapi.entity;


import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "friend_requests", uniqueConstraints = {@UniqueConstraint(columnNames = {"sender_id", "receiver_id"})})
public class FriendRequest {

    @Id
    @GeneratedValue
    private Long id;

    private Status status = Status.PENDING;

    @ManyToOne(optional = false)
    private User sender;

    @ManyToOne(optional = false)
    private User receiver;

    @Column(nullable = false, updatable = false)
    private Instant sentAt = Instant.now();

    public enum Status {
        PENDING,
        ACCEPTED
    }

    public FriendRequest(User sender, User receiver){
        this.setSender(sender);
        this.setReceiver(receiver);
    }

}
