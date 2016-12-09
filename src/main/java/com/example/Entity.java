package com.example;

import java.time.LocalDateTime;

public class Entity {

    private LocalDateTime dateTime;

    public Entity() {
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(final LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
