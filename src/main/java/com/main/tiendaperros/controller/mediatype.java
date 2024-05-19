package com.main.tiendaperros.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mediatype {

    private final ResourceLoader resourceLoader;

    public mediatype(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/images/{path}/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String path, @PathVariable String filename) {
        try {
            Resource image = resourceLoader.getResource("classpath:/static/images/" + path + "/" + filename);
            if (image.exists() || image.isReadable()) {
                String contentType = determineContentType(filename);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(image);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
