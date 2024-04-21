package com.springbootassignment.controller;

import com.springbootassignment.model.AlbumModel;
import com.springbootassignment.service.AlbumService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/albums")
    public List<AlbumModel> retrieveAllAlbums() {
        return albumService.retrieveAllAlbums();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}/albums")
    public List<AlbumModel> retrieveAlbumsByUserId(@PathVariable int id) {
        return albumService.retrieveAlbumsByUserId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}/album/{albumId}")
    public AlbumModel retrieveAlbum(@PathVariable int id, @PathVariable int albumId) {
        return albumService.retrieveAlbumByUserIdAndAlbumId(id, albumId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}/albums")
    public ResponseEntity<Object> deleteAllAlbums(@PathVariable int id) {
        return albumService.deleteAllAlbums(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}/album/{albumId}")
    public ResponseEntity<Object> deleteAlbum(@PathVariable int id, @PathVariable int albumId) {
        return albumService.deleteAlbumByUserIdAndAlbumId(id, albumId);
    }
}
