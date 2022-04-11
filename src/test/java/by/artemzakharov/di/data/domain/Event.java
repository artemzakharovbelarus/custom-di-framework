package com.vizorgames.interview.data.domain;

import java.util.Objects;

public class Event {

    private String event;

    public Event(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event1 = (Event) o;
        return Objects.equals(event, event1.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event);
    }
}
