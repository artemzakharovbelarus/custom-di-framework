package com.vizorgames.interview.data.dao.profile;

import com.vizorgames.interview.data.domain.Event;

import java.util.Collection;
import java.util.Collections;

public class InMemoryProfileDAO implements ProfileDAO
{
    @Override
    public Collection<Event> getProfiles()
    {
        return Collections.emptyList();
    }
}
