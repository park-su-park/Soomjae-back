package com.parksupark.soomjae.server.community.location.repository;

import com.parksupark.soomjae.server.community.location.entity.Location;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByName(String name);

    List<Location> findByHierarchy(int hierarchy);

    List<Location> findByParentCode(Long parentCode);
}
