package org.gauge.gradle;

import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;

public abstract class GaugeExtensionNew {
    @Inject
    public GaugeExtensionNew() {
        getEnv().convention(gradleProperty(GaugeConstants.ENVIRONMENT).getOrElse("default"));
        getSpecsDir().convention(gradleProperty(GaugeConstants.SPECS_DIR).getOrElse("specs"));
        getInParallel().convention(gradleProperty(GaugeConstants.IN_PARALLEL).map(Boolean::parseBoolean).getOrElse(false));
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
    public abstract Property<Integer> getNodes();

    @Input
    @Optional
    public abstract MapProperty<String, String> getEnvironmentVariables();

    @Input
    @Optional
    public abstract Property<String> getAdditionalFlags();

    @Input
    @Optional
    public abstract Property<String> getGaugeRoot();

    private Provider<String> gradleProperty(String name) {
        return getProviders().gradleProperty(name);
    }

}
