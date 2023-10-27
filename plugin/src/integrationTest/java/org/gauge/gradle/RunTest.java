package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunTest extends Base {

    private static final String GAUGE_PROJECT_ONE = "project1";

    @Test
    void testCanRunGaugeTasksWithDefaultConfigurations() throws IOException {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult result = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, result.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(result.getOutput(), containsString("Successfully generated html-report"));
    }

    @Test
    void testCanRunGaugeTestsFromADifferentDirectoryWhenDirPropertySet() throws IOException {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
        final File subProject = new File(Path.of(defaultGradleRunner().getProjectDir().getPath(), "subProject").toString());
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE, subProject);
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult resultWithDirProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-Pdir=" + subProject.getAbsolutePath()).build();
        assertEquals(SUCCESS, resultWithDirProperty.task(GAUGE_TASK_PATH).getOutcome());
    }

    @Test
    void testCanRunGaugeTestsFromADifferentDirectoryWhenDirSetInExtension() throws IOException {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
        final File subProject = new File(Path.of(defaultGradleRunner().getProjectDir().getPath(), "subProject").toString());
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE, subProject);
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock() + "gauge {dir=\"subProject\"}");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtensionProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "--info").build();
        assertEquals(SUCCESS, resultWithExtensionProperty.task(GAUGE_TASK_PATH).getOutcome());
    }

}
