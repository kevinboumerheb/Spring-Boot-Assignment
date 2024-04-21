package com.springbootassignment.controller;

import com.springbootassignment.model.PhotoModel;
import com.springbootassignment.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/photos")
    public List<PhotoModel> retrieveAllPhotos() {
        return photoService.retrieveAllPhotos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}/photos")
    public List<PhotoModel> retrieveAllPhotosByUserId(@PathVariable int id) {
        return photoService.retrieveAllPhotosByUserId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}/album/{albumId}/photos")
    public List<PhotoModel> retrieveAllPhotosByUserIdAndAlbumId(@PathVariable int userId, @PathVariable int albumId) {
        return photoService.retrieveAllPhotosByUserIdAndAlbumId(userId, albumId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}/album/{albumId}/photo/{photoId}")
    public PhotoModel retrievePhotoByUserIdAndAlbumId(@PathVariable int userId, @PathVariable int albumId, @PathVariable int photoId) {
        return photoService.retrievePhotoByUserIdAndAlbumId(userId, albumId, photoId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}/album/{albumId}/photos")
    public ResponseEntity<Object> deleteAllPhotosInAlbum(@PathVariable int id, @PathVariable int albumId) {
        return photoService.deleteAllPhotosByUserIdAndAlbumId(id, albumId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}/album/{albumId}/photo/{photoId}")
    public ResponseEntity<Object> deletePhoto(@PathVariable int id, @PathVariable int albumId, @PathVariable int photoId) {
        return photoService.deletePhotoByUserIdAndAlbumIdAndPhotoId(id, albumId, photoId);
    }
}
