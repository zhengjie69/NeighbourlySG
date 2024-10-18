package com.nusiss.neighbourlysg.observer;

import org.springframework.context.ApplicationEvent;

public class EventCreatedEvent extends ApplicationEvent {
    private final String eventTitle;

    public EventCreatedEvent(Object source, String evenTitle) {
        super(source);
        this.eventTitle = evenTitle;
    }

    public String getEventTitle() {
        return eventTitle;
    }
}
