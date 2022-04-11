package com.vizorgames.interview.data.dao.event;

import com.vizorgames.interview.data.domain.Event;

import java.util.List;

public interface EventDAO {
    List<Event> getEvents();
}
