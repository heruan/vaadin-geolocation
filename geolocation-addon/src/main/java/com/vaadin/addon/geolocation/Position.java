package com.vaadin.addon.geolocation;

import java.time.Instant;

public class Position {

    private final Coordinates coordinates;

    private final Instant timestamp;

    Position(long timestamp, Double latitude, Double longitude, Double altitude, Double accuracy,
        Double altitudeAccuraty, Double heading, Double speed) {
        this.timestamp = Instant.ofEpochMilli(timestamp);
        this.coordinates = new Coordinates();
        this.coordinates.latitude = latitude;
        this.coordinates.longitude = longitude;
        this.coordinates.altitude = altitude;
        this.coordinates.accuracy = accuracy;
        this.coordinates.altitudeAccuracy = altitudeAccuraty;
        this.coordinates.heading = heading;
        this.coordinates.speed = speed;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

}
