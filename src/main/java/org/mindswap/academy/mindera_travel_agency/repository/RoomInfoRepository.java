package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("dev")
public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {
    @Query(value = "DELETE FROM room_info WHERE external_id = ?1", nativeQuery = true)
    void deleteByExternalId(Long id);
}
