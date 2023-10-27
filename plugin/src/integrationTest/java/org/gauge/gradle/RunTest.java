package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.gauge.gradle.GaugeConstants.GAUGE_TASK;
import static org.gradle.testkit.runner.TaskOutcome.FAILED;
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
    void testCanRunGaugeTestsWhenDirPropertySet() throws IOException {
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
    void testCanRunGaugeTestsWhenDirSetInExtension() throws IOException {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
        final File subProject = new File(Path.of(defaultGradleRunner().getProjectDir().getPath(), "subProject").toString());
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE, subProject);
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock() + "gauge {dir=\"subProject\"}");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtensionProperty = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtensionProperty.task(GAUGE_TASK_PATH).getOutcome());
    }

    @Test
    void testCanRunGaugeTestsWhenSpecsDirSet() throws IOException {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
        // Given plugin is applied
        // When specsDir is set in the extension with an invalid/non-existing directory
        writeFile(buildFile, getApplyPluginsBlock() + "gauge {specsDir=\"invalid\"}\n");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).buildAndFail();
        // And I should get a failure with missing specs directory
        assertEquals(FAILED, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(resultWithExtension.getOutput(), containsString("Specs directory invalid does not exist."));
        // When specsDir is set to multiple specs directory with one being an invalid/non-existing directory
        BuildResult resultWithProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-PspecsDir=specs specs2").buildAndFail();
        // And I should get a failure with missing specs directory
        assertEquals(FAILED, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        assertThat(resultWithProperty.getOutput(), containsString("Specs directory specs2 does not exist."));
    }

    @Test
    void testCanRunGaugeTestsWhenEnvVariablesAndAdditionalFlagsSet() throws IOException {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
        // Given plugin is applied
        // When environmentVariables is set in extension
        // And additionalFlags include the --verbose flag
        writeFile(buildFile, getApplyPluginsBlock()
                + "gauge {environmentVariables=['customVariable': 'customValue']\n"
                + "additionalFlags='--simple-console --verbose'}\n");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see custom environment was set correctly
        assertThat(resultWithExtension.getOutput(), containsString("customVariable is set to customValue in build.gradle"));
        // And I should see the step names included in console output with --verbose flag set
        assertThat(resultWithExtension.getOutput(), containsString("The word \"gauge\" has \"3\" vowels."));
    }

    @Test
    void testCanRunGaugeTestsWhenInParallelSet() throws IOException {
        copyGaugeProjectToTemp(GAUGE_PROJECT_ONE);
        // Given plugin is applied
        // When inParallel=true is set in extension
        // And additionalFlags include the --verbose flag
        writeFile(buildFile, getApplyPluginsBlock()
                + "gauge {specsDir='specs multipleSpecs'\ninParallel=true\n"
                + "additionalFlags='--simple-console'}\n");
        // Then I should be able to run the gauge task
        BuildResult resultWithExtension = defaultGradleRunner().withArguments(GAUGE_TASK).build();
        assertEquals(SUCCESS, resultWithExtension.task(GAUGE_TASK_PATH).getOutcome());
        // And I should see tests running in default parallel streams
        assertThat(resultWithExtension.getOutput(), containsString("parallel streams."));
        // When nodes=2 project property is set
        BuildResult resultWithProperty = defaultGradleRunner().withArguments(GAUGE_TASK, "-Pnodes=2").build();
        assertEquals(SUCCESS, resultWithProperty.task(GAUGE_TASK_PATH).getOutcome());
        // Then I should see tests running in 2 parallel streams
        assertThat(resultWithProperty.getOutput(), containsString("Executing in 2 parallel streams."));
        // And I should see all 4 specifications were executed
        assertThat(resultWithProperty.getOutput(), containsString("Specifications:\t4 executed"));
    }

}
