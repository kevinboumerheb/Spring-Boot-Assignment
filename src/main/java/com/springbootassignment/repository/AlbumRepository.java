package com.springbootassignment.repository;

import com.springbootassignment.model.AlbumModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<AlbumModel, Long> {
    List<AlbumModel> findByUserId(Long userId);

    AlbumModel findByUserIdAndId(Long userId, Long albumId);
}
