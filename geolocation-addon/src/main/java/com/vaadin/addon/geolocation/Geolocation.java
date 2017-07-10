package com.vaadin.addon.geolocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.addon.geolocation.client.GeolocationClientRpc;
import com.vaadin.addon.geolocation.client.GeolocationServerRpc;
import com.vaadin.server.AbstractExtension;
import com.vaadin.shared.Registration;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class Geolocation extends AbstractExtension {

    private final GeolocationServerRpc serverRpc = new GeolocationServerRpc() {

        @Override
        public void noSupport() {
            System.out.println("No support");
        };

        @Override
        public void timeout(String message) {
            System.out.println("Timeout: " + message);
        }

        @Override
        public void positionUnavailable(String message) {
            System.out.println("Position unavailable: " + message);
        }

        @Override
        public void permissionDenied(String message) {
            System.out.println("Permission denied: " + message);
        }

        @Override
        public void callAndRemoveListener(int listenerId, Long timestamp, Double latitude, Double longitude,
            Double altitude, Double accuracy, Double altitudeAccuraty, Double heading, Double speed) {
            Geolocation.this.currentListeners.get(listenerId).accept(
                new Position(timestamp, latitude, longitude, altitude, accuracy, altitudeAccuraty, heading, speed));
            Geolocation.this.currentListeners.remove(listenerId);
        }

        @Override
        public void callWatchers(Long timestamp, Double latitude, Double longitude, Double altitude, Double accuracy,
            Double altitudeAccuraty, Double heading, Double speed) {
            Position position =
                new Position(timestamp, latitude, longitude, altitude, accuracy, altitudeAccuraty, heading, speed);
            Geolocation.this.watchListeners.forEach(consumer -> consumer.accept(position));
        }
    };

    private final List<Consumer<Position>> currentListeners = new ArrayList<>();

    private final List<Consumer<Position>> watchListeners = new ArrayList<>();

    public Geolocation(UI ui) {
        this.registerRpc(this.serverRpc, GeolocationServerRpc.class);
        this.extend(ui);
    }

    public void getCurrentPosition(Consumer<Position> listener) {
        if (!this.currentListeners.contains(listener)) {
            this.currentListeners.add(listener);
        }
        int listenerIndex = this.currentListeners.indexOf(listener);
        this.getRpcProxy(GeolocationClientRpc.class).getCurrentPosition(listenerIndex);
    }

    public Registration watchPosition(Consumer<Position> listener) {
        if (!this.watchListeners.contains(listener)) {
            this.watchListeners.add(listener);
        }
        int listenerIndex = this.watchListeners.indexOf(listener);
        this.getRpcProxy(GeolocationClientRpc.class).watchPosition(listenerIndex);
        return () -> {
            this.watchListeners.remove(listenerIndex);
            this.getRpcProxy(GeolocationClientRpc.class).clearWatch(listenerIndex);
        };
    }

}
