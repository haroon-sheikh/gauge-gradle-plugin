package org.gauge.gradle;

import java.util.List;
import java.util.Map;

class GaugeProperties {

    private final GaugeExtensionNew extension;
    private final Map<String, ?> properties;

    public GaugeProperties(final GaugeExtensionNew extension, final Map<String, ?> properties) {
        this.extension = extension;
        this.properties = properties;
    }

    public List<String> getTags() {
        System.out.println(extension.getTags().isPresent());
        return properties.containsKey("tags") ? List.of(properties.get("tags").toString())
                : extension.getTags().isPresent() ? List.of(extension.getTags().get()) : List.of();
    }

}
