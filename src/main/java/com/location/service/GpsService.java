package com.location.service;

import com.location.entity.GpsLocation;
import com.location.repository.GpsLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GpsService {

    private final GpsLocationRepository gpsLocationRepository;

    @Autowired
    public GpsService(GpsLocationRepository gpsLocationRepository) {
        this.gpsLocationRepository = gpsLocationRepository;
    }
    public GpsLocation getLocation(Double latitude, Double longitude, Double radius) {
        // GpsLocation tablosunda veriyi ara
        return gpsLocationRepository.findGpsLocationByLatitudeAndLongitudeAndRadius(latitude, longitude, radius);
    }
    public GpsLocation saveLocation(Double latitude, Double longitude, Double radius) {
        GpsLocation gpsLocation = new GpsLocation();
        gpsLocation.setLatitude(latitude);
        gpsLocation.setLongitude(longitude);
        gpsLocation.setRadius(radius);
      return gpsLocationRepository.save(gpsLocation);
    }
    public boolean validateParameters(Double latitude, Double longitude, Double radius) {
        return latitude != null && longitude != null && radius != null &&
                !Double.isNaN(latitude) && !Double.isNaN(longitude) && !Double.isNaN(radius);
    }
}
