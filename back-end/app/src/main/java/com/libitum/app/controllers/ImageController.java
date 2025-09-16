package com.libitum.app.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ImageController {
    @GetMapping("/spot-commercial")
    public ResponseEntity<byte[]> getSpotCommercial() throws IOException {
        ClassPathResource imgFile = new ClassPathResource("images/spot-commercial-logo-libitum.png");
        byte[] bytes = imgFile.getInputStream().readAllBytes();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"spot-commercial-logo-libitum.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }
}
