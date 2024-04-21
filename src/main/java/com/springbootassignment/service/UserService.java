package com.springbootassignment.service;

import com.springbootassignment.exception.NotFoundException;
import com.springbootassignment.model.AlbumModel;
import com.springbootassignment.model.PhotoModel;
import com.springbootassignment.model.UserModel;
import com.springbootassignment.repository.AlbumRepository;
import com.springbootassignment.repository.PhotoRepository;
import com.springbootassignment.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public UserService(UserRepository userRepository, AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }
    public List<UserModel> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public UserModel retrieveUserById(int id) {
        Optional<UserModel> user = userRepository.findById((long) id);

        if (user == null) {
            throw new NotFoundException("id-" + id);
        }

        return user.get();
    }

    public ResponseEntity<Object> deleteUserById(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new NotFoundException("Access denied: Only admins can delete users");
        }

        Optional<UserModel> user = userRepository.findById((long) id);
        if (!user.isPresent()) {
            throw new NotFoundException("User not found");
        }

        List<AlbumModel> albums = albumRepository.findByUserId((long) id);
        List<PhotoModel> allPhotos = new ArrayList<>();
        for (AlbumModel album : albums) {
            List<PhotoModel> photos = photoRepository.findByAlbumId(album.getId());
            allPhotos.addAll(photos);
            photoRepository.deleteAll(photos);
        }
        albumRepository.deleteAll(albums);
        userRepository.deleteById((long) id);

        Map<String, Object> response = new HashMap<>();
        response.put("deletedUser", user.get());
        response.put("deletedAlbums", albums);
        response.put("deletedPhotos", allPhotos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
