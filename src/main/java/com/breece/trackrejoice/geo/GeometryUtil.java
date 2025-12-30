package com.breece.trackrejoice.geo;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

import java.util.List;

public class GeometryUtil {
    private static final int SRID = 4326;

    public static Point parseLocation(Double lat, Double lng) {
        return parseLocation(lat, lng, new GeometryFactory(new PrecisionModel(), SRID));
    }

    public static MultiPoint makeMultiPoint(List<LatLng> latLngList) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);
        Point[] array = latLngList.stream().map(it -> parseLocation(it.lng(), it.lat(), geometryFactory)).toArray(Point[]::new);
        return geometryFactory.createMultiPoint(array);
    }

    private static Point parseLocation(Double lat, Double lng, GeometryFactory geometryFactory) {
        PackedCoordinateSequenceFactory packedCoordinateSequenceFactory = new PackedCoordinateSequenceFactory();
        return geometryFactory.createPoint(packedCoordinateSequenceFactory.create(new double[]{lng, lat}, 2));
    }
}
