package com.nusiss.neighbourlysg.listener;

import com.nusiss.neighbourlysg.observer.EventCreatedEvent;
import com.nusiss.neighbourlysg.observer.EventDeletedEvent;
import com.nusiss.neighbourlysg.observer.EventUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
public class EventNotificationListener {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;


    public EventNotificationListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleEventCreatedEvent(EventCreatedEvent event) {
        // Broadcast the event to a WebSocket topic
        messagingTemplate.convertAndSend("/topic/events", "A event named " + event.getEventTitle() + " has been created");
    }

    @EventListener
    public void handleEventUpdatedEvent(EventUpdatedEvent event) {
        messagingTemplate.convertAndSend("/topic/events", "A event named " + event.getEventTitle() + " has been updated");
    }

    @EventListener
    public void handleEventDeletedEvent(EventDeletedEvent event) {
        messagingTemplate.convertAndSend("/topic/events", "A event named " + event.getEventTitle() + " has been deleted");
    }
}
