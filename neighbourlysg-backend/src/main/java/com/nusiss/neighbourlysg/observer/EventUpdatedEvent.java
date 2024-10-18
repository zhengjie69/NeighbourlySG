package com.nusiss.neighbourlysg.observer;

import org.springframework.context.ApplicationEvent;

public class EventUpdatedEvent extends ApplicationEvent {
    private final Long eventId;

    public EventUpdatedEvent(Object source, Long eventId) {
        super(source);
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }
}
