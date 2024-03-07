package com.location.controller;

import com.jayway.jsonpath.DocumentContext;
import com.location.entity.GpsLocation;
import com.location.service.GpsService;
import com.location.service.LocationResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RequestMapping("api/gps")
@RestController
public class GpsController {

    private final WebClient webClient;
    private final GpsService gpsService;
    private final LocationResultService locationResultService;
    private final String apiKey = "AIzaSyC3Hz-woANX8fYcRga98azXCtX7Vv85IjE";

    @Autowired
    public GpsController(WebClient.Builder webClientBuilder, GpsService gpsService, LocationResultService locationResultService) {
        this.gpsService = gpsService;
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com").build();
        this.locationResultService = locationResultService;
    }

    @GetMapping("/search-places")
    public List<String> searchNearbyPlaces(
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam Double radius
    ) {
        boolean isValid = gpsService.validateParameters(latitude, longitude, radius);
        if (!isValid) {
            return Collections.singletonList("Bos veya Hatali Parametre Girdiniz.");
        }

        GpsLocation gpsLocation = gpsService.getLocation(latitude, longitude, radius);
        if (gpsLocation != null) {
            return locationResultService.getResult(gpsLocation.getId());
        } else {
            try {
                List<String> resultList = webClient.get()
                        .uri("/maps/api/place/nearbysearch/json?location={latitude},{longitude}&radius={radius}&key={apiKey}",
                                latitude, longitude, radius, apiKey)
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMap(json -> {
                            List<String> names = extractNamesFromJson(json);
                            return Mono.just(names);
                        })
                        .onErrorResume(ex -> {
                            return Mono.just(Collections.emptyList());
                        })
                        .block();

                GpsLocation location = gpsService.saveLocation(latitude, longitude, radius);
                if (resultList != null && !resultList.isEmpty()) {
                    locationResultService.saveLocationResult(location, resultList);
                }
                return resultList;
            } catch (Exception e) {
                // Hata durumunda yapılacaklar
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
    }


    public List<String> extractNamesFromJson(String json) {
        DocumentContext documentContext = JsonPath.parse(json);
        return documentContext.read("$.results[*].name");
    }
}
