package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.entity.Event;
import com.nusiss.neighbourlysg.entity.EventParticipant;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.entity.Role;

import java.time.LocalDate;
import java.util.Arrays;

public final class MasterEntityTestUtil {

    public static Profile createProfileEntity(){
        Profile profile = new Profile();
        profile.setConstituency("con");
        profile.setEmail("email");
        profile.setId(1L);
        profile.setRoles(Arrays.asList(createRoleEntity()));
        profile.setName("name");
        profile.setPassword("password");
        return profile;
    }

    public static Role createRoleEntity(){
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        return role;
    }

    public static Event createEventEntity(){
        Profile testProfile = createProfileEntity();

        Event event = new Event();
        event.setTitle("testTitle");
        event.setProfile(testProfile);
        event.setLocation("testLocation");
        event.setDescription("testDescription");
        event.setDate(LocalDate.now());
        event.setStartTime("testStartTime");
        event.setEndTime("testEndTime");

        return event;
    }

    public static EventParticipant createEventParticipantEntity() {
        Event event = createEventEntity();
        Profile profile = createProfileEntity();

        EventParticipant eventParticipant = new EventParticipant();
        eventParticipant.setEvent(event);
        eventParticipant.setProfile(profile);

        return eventParticipant;
    }
}
