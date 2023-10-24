package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GaugeTaskNew extends Test {
    private static final Logger logger = LoggerFactory.getLogger("gauge");

    @Input
    @Optional
    public abstract Property<String> getGaugeRoot();

    public GaugeTaskNew() {
        this.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        this.setDescription("Runs the Gauge test suite.");
        this.getOutputs().upToDateWhen(task -> false);
    }

    @TaskAction
    public void execute() {
        final Project project = getProject();
        final GaugeExtensionNew extension = project.getExtensions().findByType(GaugeExtensionNew.class);
        final GaugeProperties properties = new GaugeProperties(extension, project.getProperties());
        System.out.println(properties.getTags());
        logger.info("Running gauge ...");
        project.exec(spec -> {
            spec.executable("gauge" );
            spec.args("run");
            spec.args("--dir", project.getProjectDir().getAbsolutePath());
            spec.args("--simple-console");
//            spec.args("--parallel");
//            if (properties.isInParallel()) {
//            }
//            spec.args("--env", properties.getEnv());
//            spec.args("--tags", properties.getTags());
            spec.environment("gauge_custom_classpath", getClasspath().getAsPath());
            if (null != extension) {
                spec.args(extension.getSpecsDir().get());
                extension.getEnvironmentVariables().get().forEach(spec::environment);
            }
        });
    }

}
