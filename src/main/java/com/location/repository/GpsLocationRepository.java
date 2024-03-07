package com.location.repository;

import com.location.entity.GpsLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsLocationRepository extends JpaRepository<GpsLocation, Long> {

    GpsLocation findGpsLocationByLatitudeAndLongitudeAndRadius(Double latitude, Double longitude, Double radius);


}
