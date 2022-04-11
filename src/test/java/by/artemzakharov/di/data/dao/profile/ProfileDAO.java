package com.vizorgames.interview.data.dao.profile;

import com.vizorgames.interview.data.domain.Event;

import java.util.Collection;

public interface ProfileDAO
{
    Collection<Event> getProfiles();
}
