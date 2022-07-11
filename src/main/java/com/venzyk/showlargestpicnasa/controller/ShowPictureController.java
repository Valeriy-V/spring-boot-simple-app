package com.venzyk.showlargestpicnasa.controller;

import com.venzyk.showlargestpicnasa.service.LargestPictureService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
public class ShowPictureController {

    private LargestPictureService largestPictureService;

    @GetMapping("pictures/{sol}/largest")
    public ResponseEntity<Object> largestPic(@PathVariable String sol) throws URISyntaxException {
        String redirectUrl = largestPictureService.findLargestPictureUrl(sol);

        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(new URI(redirectUrl))
                .build();
    }
}
