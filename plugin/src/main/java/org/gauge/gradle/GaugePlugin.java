/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package org.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GaugePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("gaugeNew", GaugeExtensionNew.class);
        project.getExtensions().create(GaugeConstants.GAUGE_EXTENSION_ID, GaugeExtension.class);
        project.getTasks().create(GaugeConstants.GAUGE_TASK, GaugeTask.class, task -> {
            task.setGroup("verification");
            task.setDescription("Runs the Gauge test suite.");
        });
        project.getTasks().create(GaugeConstants.CLASSPATH_TASK, ClasspathTask.class);
    }
}