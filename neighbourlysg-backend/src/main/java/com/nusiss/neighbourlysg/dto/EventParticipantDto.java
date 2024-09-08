package com.nusiss.neighbourlysg.dto;

public class EventParticipantDto {

    private Long profileId;

    private Long eventId;

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
