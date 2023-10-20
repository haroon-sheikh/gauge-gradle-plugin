package org.gauge.gradle;

import org.gauge.gradle.GaugeExtension;
import org.gauge.gradle.GaugePlugin;
import org.gauge.gradle.GaugeTask;
import org.gauge.gradle.util.ProcessBuilderFactory;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GaugeTaskTest {
    private static final String GAUGE = "gauge";
    private static final String ENV_FLAG = "--env";
    private static final String TAGS_FLAG = "--tags";
    private static final String NODES_FLAG = "-n";
    private static final String VERBOSE_FLAG = "--verbose";
    private static final String SPECS_FOLDER = "specsFolder";
    private static final String PARALLEL_FLAG = "--parallel";
    private static final String GAUGE_ROOT = "/opt/gauge";

    private GaugeTask task;
    private Project project;
    private ProcessBuilderFactory factory;

    @BeforeEach
    public void setUp() {
        GaugePlugin plugin = new GaugePlugin();
        project = ProjectBuilder.builder().build();
        plugin.apply(project);
        task = (GaugeTask) project.getTasks().findByPath(GAUGE);
        factory = mock(ProcessBuilderFactory.class);
    }

    @Test
    public void shouldLoadProperties() throws InterruptedException {
        Process process = mock(Process.class);
        setExpectations(process);
        executeGaugeTask(process);

        List<String> command = factory.create().command();
        assertTrue(command.contains(Paths.get(GAUGE_ROOT, "bin", GAUGE).toString()));
        assertTrue(command.contains(PARALLEL_FLAG));
        assertTrue(command.contains(NODES_FLAG));
        assertTrue(command.contains("2"));
        assertTrue(command.contains(ENV_FLAG));
        assertTrue(command.contains("dev"));
        assertTrue(command.contains(TAGS_FLAG));
        assertTrue(command.contains("tag1"));
        assertTrue(command.contains(VERBOSE_FLAG));
        assertTrue(command.contains(SPECS_FOLDER));
    }

    private void executeGaugeTask(Process process) {
        GaugeExtension gauge = (GaugeExtension) project.getExtensions().findByName(GAUGE);
        gauge.setInParallel(true);
        gauge.setNodes(2);
        gauge.setEnv("dev");
        gauge.setTags("tag1");
        gauge.setAdditionalFlags(VERBOSE_FLAG);
        gauge.setSpecsDir(SPECS_FOLDER);
        gauge.setGaugeRoot(GAUGE_ROOT);
        gauge.environment("key", "value");
        task.executeGaugeSpecs(process);
    }

    private void setExpectations(Process process) throws InterruptedException {
        ArrayList<String> expectedCommand = new ArrayList<>();
        expectedCommand.add(Paths.get(GAUGE_ROOT, "bin", GAUGE).toString());
        expectedCommand.add(PARALLEL_FLAG);
        expectedCommand.add(NODES_FLAG);
        expectedCommand.add("2");
        expectedCommand.add(ENV_FLAG);
        expectedCommand.add("dev");
        expectedCommand.add(TAGS_FLAG);
        expectedCommand.add("tag1");
        expectedCommand.add(VERBOSE_FLAG);
        expectedCommand.add(SPECS_FOLDER);

        when(factory.create()).thenReturn(new ProcessBuilder(expectedCommand));
        when(process.waitFor()).thenReturn(0);
    }
}
