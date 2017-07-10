package com.vaadin.addon.geolocation.client;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.addon.geolocation.Geolocation;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(Geolocation.class)
@SuppressWarnings("serial")
public class GeolocationConnector extends AbstractExtensionConnector implements GeolocationServerRpc {

    private final GeolocationClientRpc clientRpc = new GeolocationClientRpc() {

        private final Map<Integer, Integer> listenerMapping = new HashMap<>();

        @Override
        public void clearWatch(int listenerId) {
            this._clearWatch(GeolocationConnector.this, this.listenerMapping.get(listenerId));
        };

        @Override
        public void getCurrentPosition(int listenerId) {
            this._getCurrentPosition(GeolocationConnector.this, listenerId);
        };

        @Override
        public void watchPosition(int listenerId) {
            int watchId = this._watchPosition(GeolocationConnector.this);
            this.listenerMapping.put(listenerId, watchId);
        };

        public native void _clearWatch(GeolocationConnector connector, int watchId)
        /*-{
            if (!("geolocation" in navigator)) {
                connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::noSupport()();
            } else {
                navigator.geolocation.clearWatch(watchId);
            }
         }-*/;

        public native void _getCurrentPosition(GeolocationConnector connector, int listenerId)
        /*-{
            if (!("geolocation" in navigator)) {
                connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::noSupport()();
            } else {
                navigator.geolocation.getCurrentPosition(function (position) {
                    connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::callAndRemoveListener(*)(listenerId, position.timestamp,
                        position.coords.latitude, position.coords.longitude, position.coords.altitude,
                        position.coords.accuracy, position.coords.altitudeAccuracy, position.coords.heading,
                        position.coords.speed);
                }, function (error) {
                    switch (error.code) {
                        case 1: connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::permissionDenied(*)(error.message); break;
                        case 2: connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::positionUnavailable(*)(error.message); break;
                        case 3: connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::timeout(*)(error.message); break;
                    }
                });
            }
        }-*/;

        public native int _watchPosition(GeolocationConnector connector)
        /*-{
            if (!("geolocation" in navigator)) {
                connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::noSupport()();
                return -1;
            } else {
                return navigator.geolocation.watchPosition(function (position) {
                    connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::callWatchers(*)(position.timestamp,
                        position.coords.latitude, position.coords.longitude, position.coords.altitude,
                        position.coords.accuracy, position.coords.altitudeAccuracy, position.coords.heading,
                        position.coords.speed);
                }, function (error) {
                    switch (error.code) {
                        case 1: connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::permissionDenied(*)(); break;
                        case 2: connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::positionUnavailable(*)(); break;
                        case 3: connector.@com.vaadin.addon.geolocation.client.GeolocationConnector::timeout(*)(); break;
                    }
                });
            }
        }-*/;

    };

    @Override
    public void callAndRemoveListener(int listenerId, Long timestamp, Double latitude, Double longitude,
        Double altitude, Double accuracy, Double altitudeAccuraty, Double heading, Double speed) {
        this.getRpcProxy(GeolocationServerRpc.class).callAndRemoveListener(listenerId, timestamp, latitude, longitude,
            altitude, accuracy, altitudeAccuraty, heading, speed);
    }

    @Override
    public void callWatchers(Long timestamp, Double latitude, Double longitude, Double altitude, Double accuracy,
        Double altitudeAccuraty, Double heading, Double speed) {
        this.getRpcProxy(GeolocationServerRpc.class).callWatchers(timestamp, latitude, longitude, altitude, accuracy,
            altitudeAccuraty, heading, speed);
    }

    @Override
    public void permissionDenied(String message) {
        this.getRpcProxy(GeolocationServerRpc.class).permissionDenied(message);
    }

    @Override
    public void positionUnavailable(String message) {
        this.getRpcProxy(GeolocationServerRpc.class).positionUnavailable(message);
    }

    @Override
    public void timeout(String message) {
        this.getRpcProxy(GeolocationServerRpc.class).timeout(message);
    }

    @Override
    public void noSupport() {
        this.getRpcProxy(GeolocationServerRpc.class).noSupport();
    }

    @Override
    protected void init() {
        super.init();
        this.registerRpc(GeolocationClientRpc.class, this.clientRpc);
    }

    @Override
    protected void extend(ServerConnector target) {}

}
