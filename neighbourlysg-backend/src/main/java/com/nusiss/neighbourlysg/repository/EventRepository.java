package com.nusiss.neighbourlysg.repository;

import com.nusiss.neighbourlysg.entity.Event;
import com.nusiss.neighbourlysg.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByProfile(Profile profile);

    List<Event> findByDateGreaterThanEqual(LocalDate date);

    List<Event> findByDateBefore(LocalDate date);

}
