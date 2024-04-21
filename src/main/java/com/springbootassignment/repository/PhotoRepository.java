package com.springbootassignment.repository;

import com.springbootassignment.model.PhotoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<PhotoModel, Long> {
    List<PhotoModel> findByAlbumId(Long albumId);
    PhotoModel findByAlbumIdAndId(Long albumId, Long id);
}
