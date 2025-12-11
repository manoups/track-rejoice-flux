package com.breece.trackrejoice.geo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;

@Entity
//@Value
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoJsonPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;

    @Version
    Short version = 0;
    @CreationTimestamp
    Date createdAt = null;
    @UpdateTimestamp
    Date updatedAt = null;

    @NotNull
    public String orderId = null;

    @NotNull
    Geometry lastSeenLocation = null;
}
