package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GaugeTaskNew extends Test {
    private static final Logger logger = LoggerFactory.getLogger("gauge");

    public GaugeTaskNew() {
        this.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
        this.setDescription("Runs the Gauge test suite.");
        // So that previous outputs of this task cannot be reused
        this.getOutputs().upToDateWhen(task -> false);
    }

    @TaskAction
    public void execute() {
        final Project project = getProject();
        final GaugeExtensionNew extension = project.getExtensions().findByType(GaugeExtensionNew.class);
        final GaugeCommand properties = new GaugeCommand(extension, project);
        logger.info("Running gauge ...");
        project.exec(spec -> {
            // Usage:
            // gauge <command> [flags] [args]
            spec.executable("gauge");
            spec.args("run");
            spec.args(properties.getProjectDir());
            spec.args(properties.getFlags());
            if (!properties.isFailedOrRepeatFlagProvided()) {
                spec.args(properties.getEnvironment());
                spec.args(properties.getTags());
                spec.args(properties.getSpecsDir());
            }
            spec.environment("gauge_custom_classpath", getClasspath().getAsPath());
            if (null != extension) {
                extension.getEnvironmentVariables().get().forEach(spec::environment);
            }
        });
    }

}
