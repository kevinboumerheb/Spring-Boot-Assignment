package com.springbootassignment.service;

import com.springbootassignment.exception.NotFoundException;
import com.springbootassignment.model.PhotoModel;
import com.springbootassignment.model.UserModel;
import com.springbootassignment.repository.PhotoRepository;
import com.springbootassignment.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import com.springbootassignment.model.AlbumModel;
import com.springbootassignment.repository.AlbumRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlbumService {
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public AlbumService(UserRepository userRepository, AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public List<AlbumModel> retrieveAllAlbums() {
        return albumRepository.findAll();
    }

    public List<AlbumModel> retrieveAlbumsByUserId(int userId) {
        List<AlbumModel> albums = albumRepository.findByUserId((long) userId);

        if (albums != null) {
            return albums;
        } else {
            throw new NotFoundException("userId-" + userId);
        }
    }

    public AlbumModel retrieveAlbumByUserIdAndAlbumId(int userId, int albumId) {
        AlbumModel album = albumRepository.findByUserIdAndId((long) userId, (long) albumId);

        if (album != null) {
            return album;
        } else {
            Optional<UserModel> user = userRepository.findById((long) userId);
            if (!user.isPresent()) {
                throw new NotFoundException("User with Id: " + userId + " does not exist.");
            } else {
                throw new NotFoundException("Album with Id: " + albumId + " does not exist for User Id: " + userId);
            }
        }
    }

    public ResponseEntity<Object> deleteAllAlbums(int userId) {
        Optional<UserModel> user = userRepository.findById((long) userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User with Id: " + userId + " does not exist.");
        }

        List<AlbumModel> albums = albumRepository.findByUserId((long) userId);
        if (albums.isEmpty()) {
            throw new NotFoundException("No albums found for User Id: " + userId);
        }

        List<PhotoModel> allPhotos = new ArrayList<>();
        for (AlbumModel album : albums) {
            List<PhotoModel> photos = photoRepository.findByAlbumId(album.getId());
            allPhotos.addAll(photos);
            photoRepository.deleteAll(photos);
        }

        albumRepository.deleteAll(albums);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedAlbums", albums);
        response.put("deletedPhotos", allPhotos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteAlbumByUserIdAndAlbumId(int userId, int albumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new NotFoundException("Access denied: Only admins can delete albums");
        }

        AlbumModel album = albumRepository.findByUserIdAndId((long) userId, (long) albumId);
        if (album == null) {
            throw new NotFoundException("Album not found for User Id: " + userId);
        }

        List<PhotoModel> photos = photoRepository.findByAlbumId(album.getId());
        photoRepository.deleteAll(photos);

        albumRepository.delete(album);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedAlbum", album);
        response.put("deletedPhotos", photos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
