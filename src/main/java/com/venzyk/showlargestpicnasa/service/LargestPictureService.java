package com.venzyk.showlargestpicnasa.service;

import com.venzyk.showlargestpicnasa.model.Photos;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class LargestPictureService {

    @Inject
    private RestTemplate restTemplate;
    //https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=15&api_key=DEMO_KEY
    final String baseUri = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
    final String SOL = "sol";
    final String APY_KEY = "api_key";
    final String DEMO_KEY = "DEMO_KEY";

    @Cacheable("largest-picture")
    public String findLargestPictureUrl(String sol) {
        String uri = UriComponentsBuilder.fromUriString(baseUri)
                .queryParam(SOL, sol)
                .queryParam(APY_KEY, DEMO_KEY)
                .build().toUriString();

        Map<String, Long> sizeToUrl = new HashMap<>();

        ResponseEntity<Photos> response = restTemplate.getForEntity(uri, Photos.class);
        Objects.requireNonNull(response.getBody()).getPhotos().forEach(photo -> {
            String photoUrl = photo.getUrl();
            sizeToUrl.put(photoUrl, getSize(photoUrl));
        });
        Map.Entry<String, Long> max = sizeToUrl.entrySet().stream().max(Map.Entry.comparingByValue()).get();

        return max.getKey();
    }

    private Long getSize(String url) {
        URI uri = restTemplate.headForHeaders(url).getLocation();
        Objects.requireNonNull(uri);

        return restTemplate.headForHeaders(uri).getContentLength();
    }
}

