package com.nusiss.neighbourlysg.repository;

import com.nusiss.neighbourlysg.entity.Event;
import com.nusiss.neighbourlysg.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByProfile(Profile profile);

    @Query("SELECT e FROM Event e WHERE e.date >= :date " +
            "AND e.profile.id != :profileId " +
            "AND e.profile.constituency = :constituency " +
            "AND (:location IS NULL OR :location = '' OR e.location LIKE CONCAT('%', :location, '%'))")
    List<Event> findByDateGreaterThanEqualAndNotOwnedBy(@Param("date") LocalDate date,
                                                        @Param("profileId") Long profileId,
                                                        @Param("constituency") String constituency,
                                                        @Param("location") String location);

    @Query("SELECT e FROM Event e WHERE e.date < :date AND e.profile.id != :profileId " +
            "AND e.profile.constituency = :constituency " +
            "AND (:location IS NULL OR :location = '' OR e.location LIKE CONCAT('%', :location, '%'))")
    List<Event> findByDateBeforeAndNotOwnedBy(@Param("date") LocalDate date,
                                              @Param("profileId") Long profileId,
                                              @Param("constituency") String constituency,
                                              @Param("location") String location);

}
