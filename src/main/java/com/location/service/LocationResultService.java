package com.location.service;

import com.location.entity.GpsLocation;
import com.location.entity.LocationResult;
import com.location.repository.LocationResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationResultService {
    private final LocationResultRepository locationResultRepository;

    @Autowired
    public LocationResultService(LocationResultRepository locationResultRepository) {
        this.locationResultRepository = locationResultRepository;
    }
    

    public void saveLocationResult(GpsLocation gpsLocation, List<String> resultList) {
        for (String name : resultList) {
            LocationResult locationResult = new LocationResult();
            locationResult.setGpsLocation(gpsLocation);
            locationResult.setLocationName(name);
            locationResultRepository.save(locationResult);
        }
    }

    public List<String> getResult(Long id) {
        List<LocationResult> locationResults = locationResultRepository.findByGpsLocationId(id);
        return locationResults.stream().map(LocationResult::getLocationName).collect(Collectors.toList());
    }
}
