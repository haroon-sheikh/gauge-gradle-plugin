package org.gauge.gradle;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GaugePluginTest {
    private static final String GAUGE_PLUGIN_ID = "org.gauge";
    private static final String GAUGE_TASK = "gauge";
    private Project project;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    public void pluginShouldBeAddedOnApply() {
        project.getPluginManager().apply(GAUGE_PLUGIN_ID);
        assertTrue(project.getPlugins().getPlugin(GAUGE_PLUGIN_ID) instanceof GaugePlugin);
        assertFalse(project.getPlugins().getPlugin(GAUGE_PLUGIN_ID) instanceof JavaPlugin);
    }

    @Test
    public void taskShouldBeAddedOnApply() {
        project.getPluginManager().apply(GAUGE_PLUGIN_ID);
        TaskContainer tasks = project.getTasks();
        assertEquals(2, tasks.size());

        SortedMap<String, Task> tasksMap = tasks.getAsMap();
        Task gauge = tasksMap.get(GAUGE_TASK);
        Task classpath = tasksMap.get("classpath");
        assertEquals("verification", gauge.getGroup());
        assertTrue(gauge instanceof GaugeTask);
        assertTrue(classpath instanceof ClasspathTask);
    }
}