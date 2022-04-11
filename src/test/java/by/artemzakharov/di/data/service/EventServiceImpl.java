package com.vizorgames.interview.data.service;

import com.vizorgames.interview.data.dao.event.EventDAO;
import com.vizorgames.interview.data.domain.Event;

import javax.inject.Inject;
import java.util.List;

public class EventServiceImpl implements EventService {

    private final EventDAO dao;

    @Inject
    public EventServiceImpl(EventDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Event> getEvents() {
        return dao.getEvents();
    }
}
