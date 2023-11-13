package com.example.consumer.domain;

import javax.persistence.Entity;

@Entity
public class FailedEvent {

    private Long id;

    private Long userId;

    public FailedEvent(){

    }

    public FailedEvent(Long userId) {
        this.userId = userId;
    }
}
