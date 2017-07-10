package com.vaadin.addon.geolocation.client;

import com.vaadin.shared.communication.ServerRpc;

public interface GeolocationServerRpc extends ServerRpc {

    void callAndRemoveListener(int listenerId, Long timestamp, Double latitude, Double longitude, Double altitude,
        Double accuracy, Double altitudeAccuraty, Double heading, Double speed);

    void callWatchers(Long timestamp, Double latitude, Double longitude, Double altitude, Double accuracy,
        Double altitudeAccuraty, Double heading, Double speed);

    void permissionDenied(String message);

    void positionUnavailable(String message);

    void timeout(String message);

    void noSupport();

}
