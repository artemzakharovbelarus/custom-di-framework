package com.vizorgames.interview.data.dao.event;

import com.vizorgames.interview.data.domain.Event;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InMemoryEventDAO implements EventDAO {

    private List<Event> events = Arrays.asList(new Event("1 event"), new Event("2 event"), new Event("3 event"));

    @Override
    public List<Event> getEvents() {
        return events;
    }
}
