package com.springbootassignment.service;

import com.springbootassignment.exception.NotFoundException;
import com.springbootassignment.model.AlbumModel;
import com.springbootassignment.model.PhotoModel;
import com.springbootassignment.model.UserModel;
import com.springbootassignment.repository.PhotoRepository;
import com.springbootassignment.repository.UserRepository;
import com.springbootassignment.repository.AlbumRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PhotoService {
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public PhotoService(UserRepository userRepository, AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public List<PhotoModel> retrieveAllPhotos() {
        return photoRepository.findAll();
    }

    public List<PhotoModel> retrieveAllPhotosByUserId(int userId) {
        Optional<UserModel> user = userRepository.findById((long) userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User with Id: " + userId + " does not exist.");
        }

        List<AlbumModel> albums = albumRepository.findByUserId((long) userId);
        if (albums.isEmpty()) {
            throw new NotFoundException("No albums found for User Id: " + userId);
        }

        List<PhotoModel> photos = new ArrayList<>();
        for (AlbumModel album : albums) {
            photos.addAll(photoRepository.findByAlbumId(album.getId()));
        }

        if (photos.isEmpty()) {
            throw new NotFoundException("No photos found for User Id: " + userId);
        }

        return photos;
    }

    public List<PhotoModel> retrieveAllPhotosByUserIdAndAlbumId(int userId, int albumId) {
        Optional<UserModel> user = userRepository.findById((long) userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User with Id: " + userId + " does not exist.");
        }

        List<AlbumModel> album = albumRepository.findByUserId((long) userId);
        if (album.isEmpty()) {
            throw new NotFoundException("Album with Id: " + albumId + " does not exist for User Id: " + userId);
        }

        List<PhotoModel> photos = photoRepository.findByAlbumId((long) albumId);
        if (photos.isEmpty()) {
            throw new NotFoundException("No photos found for Album Id: " + albumId);
        }

        return photos;
    }

    public PhotoModel retrievePhotoByUserIdAndAlbumId(int userId, int albumId, int photoId) {
        Optional<UserModel> user = userRepository.findById((long) userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User with Id: " + userId + " does not exist.");
        }

        List<AlbumModel> album = albumRepository.findByUserId((long) userId);
        if (album.isEmpty()) {
            throw new NotFoundException("Album with Id: " + albumId + " does not exist for User Id: " + userId);
        }

        PhotoModel photo = photoRepository.findByAlbumIdAndId((long) albumId, (long) photoId);
        if (photo == null) {
            throw new NotFoundException("Photo with Id: " + photoId + " does not exist for Album Id: " + albumId);
        }

        return photo;
    }

    public ResponseEntity<Object> deleteAllPhotosByUserIdAndAlbumId(int userId, int albumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new NotFoundException("Access denied: Only admins can delete users");
        }

        Optional<UserModel> user = userRepository.findById((long) userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User with Id: " + userId + " does not exist.");
        }

        AlbumModel album = albumRepository.findByUserIdAndId((long) userId, (long) albumId);
        if (album == null) {
            throw new NotFoundException("Album with Id: " + albumId + " does not exist for User Id: " + userId);
        }

        List<PhotoModel> photos = photoRepository.findByAlbumId(album.getId());

        if (photos.isEmpty()) {
            throw new NotFoundException("No photos found for User Id: " + userId + " and Album Id: " + albumId);
        }

        photoRepository.deleteAll(photos);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedPhotos", photos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> deletePhotoByUserIdAndAlbumIdAndPhotoId(int userId, int albumId, int photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new NotFoundException("Access denied: Only admins can delete users");
        }

        Optional<UserModel> user = userRepository.findById((long) userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User with Id: " + userId + " does not exist.");
        }

        AlbumModel album = albumRepository.findByUserIdAndId((long) userId, (long) albumId);
        if (album == null) {
            throw new NotFoundException("Album with Id: " + albumId + " does not exist for User Id: " + userId);
        }

        PhotoModel photo = photoRepository.findByAlbumIdAndId(album.getId(), (long) photoId);

        if (photo == null) {
            throw new NotFoundException("Photo not found for User Id: " + userId + ", Album Id: " + albumId + " and Photo Id: " + photoId);
        }

        photoRepository.delete(photo);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedPhoto", photo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}