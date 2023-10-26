/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package org.gauge.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaLibraryPlugin;

public class GaugePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(JavaLibraryPlugin.class);
        project.getExtensions().create("gaugeOld", GaugeExtension.class);
        project.getExtensions().create(GaugeConstants.GAUGE_EXTENSION_ID, GaugeExtensionNew.class);
        project.getTasks().create(GaugeConstants.GAUGE_TASK, GaugeTaskNew.class);
        project.getTasks().create(GaugeConstants.GAUGE_VALIDATE_TASK, GaugeValidateTask.class);
        project.getTasks().create("gaugeOld", GaugeTask.class, task -> {
            task.setGroup(GaugeConstants.GAUGE_TASK_GROUP);
            task.setDescription("Runs the Gauge test suite.");
        });
        project.getTasks().create(GaugeConstants.GAUGE_CLASSPATH_TASK, GaugeClasspathTask.class);
    }
}