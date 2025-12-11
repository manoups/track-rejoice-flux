package com.breece.trackrejoice.geo.repositories;

import com.breece.trackrejoice.geo.model.GeoJsonPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoJsonPostRepository extends JpaRepository<GeoJsonPost, Long> {}
