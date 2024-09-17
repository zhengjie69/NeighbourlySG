package com.nusiss.neighbourlysg.repository;

import com.nusiss.neighbourlysg.entity.Event;
import com.nusiss.neighbourlysg.entity.EventParticipant;
import com.nusiss.neighbourlysg.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {

    long countByEvent(Event event);

    boolean existsByEventAndProfile(Event event, Profile profile);

    Optional<EventParticipant> findByEventAndProfile(Event event, Profile profile);

}
