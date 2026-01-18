package com.breece.geo.repositories;

import com.breece.geo.model.GeoJsonPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeoJsonPostRepository extends JpaRepository<GeoJsonPost, Long> {
    Optional<GeoJsonPost> findByContentId(String contentId);
    Optional<GeoJsonPost> findBySightingId(String sightingId);
}
