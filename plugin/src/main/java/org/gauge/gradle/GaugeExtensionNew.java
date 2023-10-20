package org.gauge.gradle;

import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.CompileClasspath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;
import java.util.Map;

public abstract class GaugeExtensionNew {
    @Inject
    public GaugeExtensionNew() {
        getEnv().convention(gradleProperty(GaugeConstants.ENVIRONMENT).getOrElse("default"));
        getTags().convention(gradleProperty(GaugeConstants.TAGS).getOrNull());
        getSpecsDir().convention(gradleProperty(GaugeConstants.SPECS_DIR).getOrElse("specs"));
        getInParallel().convention(gradleProperty(GaugeConstants.IN_PARALLEL).map(Boolean::parseBoolean).getOrElse(false));
        getNodes().convention(gradleProperty(GaugeConstants.NODES).getOrNull());
        getEnvironmentVariables().convention(Map.of());
        getAdditionalFlags().convention(gradleProperty(GaugeConstants.ADDITIONAL_FLAGS).getOrNull());
        getGaugeRoot().convention(gradleProperty(GaugeConstants.GAUGE_ROOT).getOrNull());
    }

    @Inject
    protected abstract ProviderFactory getProviders();

    @Input
    @Optional
    public abstract Property<String> getEnv();

    @Input
    @Optional
    public abstract Property<String> getTags();

    @Input
    @Optional
    public abstract Property<String> getSpecsDir();

    @Input
    @Optional
    public abstract Property<Boolean> getInParallel();

    @Input
    @Optional
    public abstract Property<String> getNodes();

    @Input
    @Optional
    public abstract MapProperty<String, String> getEnvironmentVariables();

    @Input
    @Optional
    public abstract Property<String> getAdditionalFlags();

    @Input
    @Optional
    public abstract Property<String> getGaugeRoot();

    @CompileClasspath
    public abstract Property<String> getClasspath();

    private Provider<String> gradleProperty(String name) {
        return getProviders().gradleProperty(name);
    }

}
