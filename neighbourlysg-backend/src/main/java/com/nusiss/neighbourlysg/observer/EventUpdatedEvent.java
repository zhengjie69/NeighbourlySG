package com.nusiss.neighbourlysg.observer;

import org.springframework.context.ApplicationEvent;

public class EventUpdatedEvent extends ApplicationEvent {
    private final String eventTitle;

    public EventUpdatedEvent(Object source, String eventTitle) {
        super(source);
        this.eventTitle = eventTitle;
    }

    public String getEventTitle() {
        return eventTitle;
    }
}
