package com.vaadin.addon.geolocation.demo;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.geolocation.Coordinates;
import com.vaadin.addon.geolocation.Geolocation;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Push
@Theme("demo")
@Title("Geolocation Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        final Geolocation geolocation = new Geolocation(this);

        TextField timestampField = new TextField("Timestamp");
        timestampField.setReadOnly(true);
        timestampField.setPlaceholder("N/A");
        TextField latitudeField = new TextField("Latitude");
        latitudeField.setReadOnly(true);
        latitudeField.setPlaceholder("N/A");
        TextField longitudeField = new TextField("Longitude");
        longitudeField.setReadOnly(true);
        longitudeField.setPlaceholder("N/A");
        TextField accuracyField = new TextField("Accuracy (meters)");
        accuracyField.setReadOnly(true);
        accuracyField.setPlaceholder("N/A");
        TextField altitudeField = new TextField("Altitude (meters)");
        altitudeField.setReadOnly(true);
        altitudeField.setPlaceholder("N/A");
        TextField altitudeAccuracyField = new TextField("Altitude accuracy (meters)");
        altitudeAccuracyField.setReadOnly(true);
        altitudeAccuracyField.setPlaceholder("N/A");
        TextField headingField = new TextField("Heading (degrees)");
        headingField.setReadOnly(true);
        headingField.setPlaceholder("N/A");
        TextField speedField = new TextField("Speed (m/s)");
        speedField.setReadOnly(true);
        speedField.setPlaceholder("N/A");

        ProgressBar indicator = new ProgressBar();
        indicator.setIndeterminate(true);
        indicator.setVisible(false);

        Function<Object, String> stringify = o -> Optional.ofNullable(o).map(Object::toString).orElse("");
        final Button currentPositionButton = new Button("Current position", VaadinIcons.MAP_MARKER);
        currentPositionButton.addClickListener(click -> {
            indicator.setVisible(true);
            geolocation.getCurrentPosition(position -> {
                currentPositionButton.setEnabled(true);
                indicator.setVisible(false);
                timestampField.setValue(
                    position.getTimestamp().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT));
                Coordinates coordinates = position.getCoordinates();
                latitudeField.setValue(stringify.apply(coordinates.getLatitude()));
                longitudeField.setValue(stringify.apply(coordinates.getLongitude()));
                accuracyField.setValue(stringify.apply(coordinates.getAccuracy()));
                altitudeField.setValue(stringify.apply(coordinates.getAltitude()));
                altitudeAccuracyField.setValue(stringify.apply(coordinates.getAltitudeAccuracy()));
                headingField.setValue(stringify.apply(coordinates.getHeading()));
                speedField.setValue(stringify.apply(coordinates.getSpeed()));
            });
        });
        currentPositionButton.setDisableOnClick(true);

        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setSpacing(false);

        HorizontalLayout row1 = new HorizontalLayout(latitudeField, longitudeField);
        HorizontalLayout row2 = new HorizontalLayout(altitudeField, accuracyField);
        HorizontalLayout row3 = new HorizontalLayout(headingField, speedField);

        currentPositionButton.setWidth(100, Unit.PERCENTAGE);
        timestampField.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout h = new HorizontalLayout();
        h.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        h.addComponents(currentPositionButton, indicator);

        VerticalLayout panelLayout = new VerticalLayout(h, timestampField, row1, row2, row3);

        Panel panel = new Panel(panelLayout);
        panel.setWidthUndefined();

        layout.addComponent(panel);

        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        this.setContent(layout);
    }
}
