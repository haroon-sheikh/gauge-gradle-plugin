package org.gauge.gradle;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class GaugeCommand {

    private final GaugeExtensionNew extension;
    private final Map<String, ?> properties;
    private final Project project;

    public GaugeCommand(final GaugeExtensionNew extension, final Project project) {
        this.extension = extension;
        this.project = project;
        this.properties = project.getProperties();
    }

    public List<String> getProjectDir() {
        return List.of("--dir", project.getProjectDir().getAbsolutePath());
    }

    public List<String> getEnvironment() {
        return List.of("--env", getEnv());
    }

    private String getEnv() {
        return project.hasProperty(GaugeConstants.ENVIRONMENT)
                ? properties.get(GaugeConstants.ENVIRONMENT).toString()
                : extension.getEnv().get();
    }

    public boolean isFailedOrRepeatFlagProvided() {
        final List<String> flags = getAdditionalFlags();
        return flags.contains("--failed") || flags.contains("--repeat");
    }

    public List<Object> getFlags() {
        final List<Object> flags = new ArrayList<>(getAdditionalFlags());
        // --repeat and --failed flags cannot be run with other flags
        if (!isFailedOrRepeatFlagProvided()) {
            if (isInParallel()) {
                flags.add("--parallel");
                final int nodes = getNodes();
                if (nodes != 0) {
                    flags.addAll(List.of("-n", nodes));
                }
            }
        }
        return flags;
    }

    private List<String> getAdditionalFlags() {
        return project.hasProperty(GaugeConstants.ADDITIONAL_FLAGS)
                ? getListFromString(properties.get(GaugeConstants.ADDITIONAL_FLAGS).toString())
                : extension.getAdditionalFlags().isPresent()
                ? getListFromString(extension.getAdditionalFlags().get())
                : List.of();
    }

    private List<String> getListFromString(final String value) {
        return Arrays.stream(value.split("\\s+")).map(String::trim).collect(Collectors.toList());
    }

    private int getNodes() {
        return project.hasProperty(GaugeConstants.NODES)
                ? Integer.parseInt(properties.get(GaugeConstants.NODES).toString())
                : extension.getNodes().isPresent() ? extension.getNodes().get() : 0;
    }

    private boolean isInParallel() {
        return project.hasProperty(GaugeConstants.IN_PARALLEL)
                ? Boolean.parseBoolean(project.getProperties().get(GaugeConstants.IN_PARALLEL).toString())
                : extension.getInParallel().get();
    }

    public List<String> getSpecsDir() {
        return List.of(properties.containsKey(GaugeConstants.SPECS_DIR)
                ? properties.get(GaugeConstants.SPECS_DIR).toString() : extension.getSpecsDir().get());
    }

    public List<String> getTags() {
        final String tags = project.hasProperty(GaugeConstants.TAGS)
                ? properties.get(GaugeConstants.TAGS).toString()
                : extension.getTags().isPresent() ? extension.getTags().get() : "";
        return !tags.isEmpty() ? List.of("--tags", tags) : List.of();
    }

}
