package com.tsystems.timetable.producer;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyProducer {
    private Properties properties;
    @Property
    @Produces
    public String produceString(final InjectionPoint ip) {
        return this.properties.getProperty(getKey(ip));
    }
    @Property
    @Produces
    public int produceInt(final InjectionPoint ip) {
        return Integer.valueOf(this.properties.getProperty(getKey(ip)));
    }
    @Property
    @Produces
    public boolean produceBoolean(final InjectionPoint ip) {
        return Boolean.valueOf(this.properties.getProperty(getKey(ip)));
    }

    @Property
    @Produces
    public Properties produceProperties(final InjectionPoint ip) {
        String key = getKey(ip);
        Properties properties = new Properties();
        this.properties.stringPropertyNames().stream()
                .filter(property -> property.startsWith(key.concat(".")))
                .map(property -> property.substring(property.indexOf(key) + key.length() + 1))
                .forEach(property -> properties.put(property, this.properties.getProperty(key + "." + property)));
        return properties;
    }



    private String getKey(final InjectionPoint ip) {
        return (ip.getAnnotated()
                .isAnnotationPresent(Property.class) &&
                !ip.getAnnotated()
                        .getAnnotation(Property.class)
                        .value().isEmpty()) ? ip.getAnnotated()
                        .getAnnotation(Property.class)
                        .value() : ip.getMember()
                .getName();
    }
    @PostConstruct
    public void init() {
        this.properties = new Properties();
        final InputStream stream = PropertyProducer.class
                .getResourceAsStream("/application.properties");
        if (stream == null) {
            throw new RuntimeException("No properties!!!");
        }
        try {
            this.properties.load(stream);
        } catch (final IOException e) {
            throw new RuntimeException("Configuration could not be loaded!");
        }
    }
}
