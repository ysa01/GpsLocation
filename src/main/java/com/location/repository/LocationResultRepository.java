package com.location.repository;

import com.location.entity.LocationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationResultRepository extends JpaRepository<LocationResult, Long> {
    List<LocationResult> findByGpsLocationId(Long id);
}

