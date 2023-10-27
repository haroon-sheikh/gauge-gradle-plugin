package org.gauge.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunTest extends Base {

    @Test
    void testCanRunGaugeTasksWithDefaultConfigurations() throws IOException {
        copyGaugeProjectToTemp("project1");
        // Given plugin is applied
        writeFile(buildFile, getApplyPluginsBlock());
        // Then I should be able to run the gauge task
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("gauge")
                .withPluginClasspath()
                .build();
        assertThat(result.getOutput(), containsString("Successfully generated html-report"));
        assertEquals(SUCCESS, result.task(":gauge").getOutcome());
    }

}
