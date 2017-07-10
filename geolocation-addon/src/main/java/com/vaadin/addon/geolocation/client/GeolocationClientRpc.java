package com.vaadin.addon.geolocation.client;

import com.vaadin.shared.communication.ClientRpc;

public interface GeolocationClientRpc extends ClientRpc {

    void clearWatch(int listenerId);

    void getCurrentPosition(int listenerId);

    void watchPosition(int listenerId);

}
