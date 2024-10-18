package com.nusiss.neighbourlysg.observer;

import org.springframework.context.ApplicationEvent;

public class EventCreatedEvent extends ApplicationEvent {
    private final Long eventId;

    public EventCreatedEvent(Object source, Long eventId) {
        super(source);
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }
}
