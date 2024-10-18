package com.nusiss.neighbourlysg.observer;

import org.springframework.context.ApplicationEvent;

public class EventDeletedEvent extends ApplicationEvent {
    private final String eventTitle;

    public EventDeletedEvent(Object source, String eventTitle) {
        super(source);
        this.eventTitle = eventTitle;
    }

    public String getEventTitle() {
        return eventTitle;
    }
}
